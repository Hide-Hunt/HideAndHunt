package ch.epfl.sdp.replay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.FragmentReplayBinding
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.replay.game_event.CatchEvent
import ch.epfl.sdp.replay.game_event.LocationEvent
import ch.epfl.sdp.replay.steps.*
import ch.epfl.sdp.utils.toBoundingBox
import ch.epfl.sdp.utils.toLatLong
import org.mapsforge.core.graphics.Color
import org.mapsforge.core.graphics.Paint
import org.mapsforge.core.graphics.Style
import org.mapsforge.core.util.LatLongUtils.zoomForBounds
import org.mapsforge.map.android.graphics.AndroidBitmap
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.datastore.MapDataStore
import org.mapsforge.map.layer.overlay.Marker
import org.mapsforge.map.layer.overlay.Polyline
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.File


class ReplayFragment : Fragment() {
    private var _binding: FragmentReplayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReplayViewModel by activityViewModels()
    private lateinit var history: GameHistory
    private val playerMarkers = HashMap<Int, Marker>()
    private val playerPaths = HashMap<Int, Polyline>() // TODO merge this with playerMarkers

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentReplayBinding.inflate(inflater)

        arguments?.let {
            history = it.getSerializable(ARG_HISTORY) as GameHistory
        }

        mapSetup()

        return binding.root
    }

    private fun mapSetup() {
        mapsforgeInit()

        // Preparing icons
        val predatorIcon = AndroidBitmap(requireContext().getDrawable(R.drawable.ic_eye_icon)!!.toBitmap())
        val preyIcon = AndroidBitmap(requireContext().getDrawable(R.drawable.ic_target_icon)!!.toBitmap())
        val caughtIcon = AndroidBitmap(requireContext().getDrawable(R.drawable.ic_skull_icon)!!.toBitmap())

        val lastPos = HashMap<Int, Location>()
        playersInit(lastPos, predatorIcon, preyIcon)

        val steps = ArrayList<ReplayStep>()
        replayStepsInit(steps, lastPos, caughtIcon)

        val grc = GameReplayController(history.events.first().timestamp, steps)
        viewModel.timeCursor.observe(viewLifecycleOwner, Observer { grc.goToTime(it) })
    }

    private fun mapsforgeInit() {
        try {
            val mapView = binding.map
            mapView.mapScaleBar.isVisible = true
            mapView.setBuiltInZoomControls(true)

            val tileCache = AndroidUtil.createTileCache(context, "mapcache",
                    mapView.model.displayModel.tileSize, 1f,
                    mapView.model.frameBufferModel.overdrawFactor)

            val mapFile = File(requireContext().getExternalFilesDir(null), MAP_FILE)
            val mapDataStore: MapDataStore = MapFile(mapFile)
            val tileRendererLayer = TileRendererLayer(tileCache, mapDataStore,
                    mapView.model.mapViewPosition, AndroidGraphicFactory.INSTANCE)
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT)

            mapView.layerManager.layers.add(tileRendererLayer)

            val center = viewModel.trackedPlayer.value?.let { playerID ->
                history.players[playerID].lastKnownLocation
            } ?: history.bounds.center

            mapView.setCenter(center.toLatLong())
            binding.root.post {
                mapView.setZoomLevel(zoomForBounds(mapView.dimension, history.bounds.toBoundingBox(), mapView.model.displayModel.tileSize))
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error while loading the map", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun createPaint(color: Int, strokeWidth: Int, style: Style?): Paint {
        val paint: Paint = AndroidGraphicFactory.INSTANCE.createPaint()
        paint.color = color
        paint.strokeWidth = strokeWidth.toFloat()
        paint.setStyle(style)
        return paint
    }

    private fun playersInit(lastPos: HashMap<Int, Location>, predatorIcon: AndroidBitmap, preyIcon: AndroidBitmap) {
        for (player in history.players) {
            val loc = player.lastKnownLocation ?: history.bounds.center
            lastPos[player.id] = loc
            val icon = if (player is Predator) predatorIcon else preyIcon
            playerMarkers[player.id] = Marker(loc.toLatLong(), icon, 0, 0).also {
                binding.map.layerManager.layers.add(it)
            }
            playerPaths[player.id] = Polyline(createPaint(
                    AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE),
                    (8 * binding.map.model.displayModel.scaleFactor).toInt(),
                    Style.STROKE), AndroidGraphicFactory.INSTANCE).also {
                binding.map.layerManager.layers.add(it)
            }
        }
    }

    private fun replayStepsInit(steps: ArrayList<ReplayStep>, lastPos: HashMap<Int, Location>, caughtIcon: AndroidBitmap) {
        val eventsByTimestamp = history.events.groupBy { event -> event.timestamp }.toSortedMap()
        var lastStepTimestamp = eventsByTimestamp.keys.min()!!
        for ((timestamp, events) in eventsByTimestamp) {
            // Fill empty timestamps with NOP steps
            while (lastStepTimestamp < timestamp) {
                steps.add(NOPStep())
                lastStepTimestamp++
            }
            lastStepTimestamp++

            // Generate step
            val step = events.map { event ->
                when (event) {
                    is LocationEvent ->
                        LocationStep(playerMarkers[event.playerID]!!, playerPaths[event.playerID]!!, lastPos[event.playerID]!!, event.location)
                                .also { lastPos[event.playerID] = event.location }

                    is CatchEvent -> SetBitmapStep(playerMarkers[event.preyID]!!, caughtIcon)

                    else -> NOPStep()
                }
            }
            steps.add(BatchStep(step))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.destroyAll()
    }

    companion object {
        private const val ARG_HISTORY = "history"
        private const val MAP_FILE = "cache.map"

        fun newInstance(history: GameHistory) =
                ReplayFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_HISTORY, history)
                    }
                }
    }
}
