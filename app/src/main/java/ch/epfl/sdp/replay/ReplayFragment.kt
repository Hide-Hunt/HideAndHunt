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
import ch.epfl.sdp.toLatLong
import org.mapsforge.map.android.graphics.AndroidBitmap
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.datastore.MapDataStore
import org.mapsforge.map.layer.overlay.Marker
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
            mapView.setZoomLevel(18.toByte())
        } catch (e: Exception) {
            Toast.makeText(context, "Error while loading the map", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

        // Preparing icons
        val predatorIcon = AndroidBitmap(requireContext().getDrawable(R.drawable.ic_eye_icon)!!.toBitmap())
        val preyIcon = AndroidBitmap(requireContext().getDrawable(R.drawable.ic_target_icon)!!.toBitmap())
        val caughtIcon = AndroidBitmap(requireContext().getDrawable(R.drawable.ic_skull_icon)!!.toBitmap())

        val lastPos = HashMap<Int, Location>()
        for (player in history.players) {
            val loc = player.lastKnownLocation ?: history.bounds.center
            lastPos[player.id] = loc
            val icon = if (player is Predator) predatorIcon else preyIcon
            playerMarkers[player.id] = Marker(loc.toLatLong(), icon, 0, 0).also {
                binding.map.layerManager.layers.add(it)
            }
        }

        val eventsByTimestamp = history.events.groupBy{event -> event.timestamp}.toSortedMap()

        val commands = ArrayList<ReplayStep>()
        var lastStepTimestamp = eventsByTimestamp.keys.min()!!
        for ((timestamp, events) in eventsByTimestamp) {
            // Fill empty timestamps with NOP steps
            while (lastStepTimestamp < timestamp) {
                commands.add(NOPStep())
                lastStepTimestamp++
            }
            lastStepTimestamp++

            // Generate step
            val step = events.map {event ->
                when (event) {
                    is LocationEvent ->
                        LocationStep(playerMarkers[event.playerID]!!, lastPos[event.playerID]!!, event.location)
                                .also { lastPos[event.playerID] = event.location }

                    is CatchEvent -> SetBitmapStep(playerMarkers[event.preyID]!!, caughtIcon)

                    else -> NOPStep()
                }
            }
            commands.add(BatchStep(step))
        }

        val grc = GameReplayController(history.events.first().timestamp, commands)
        viewModel.timeCursor.observe(viewLifecycleOwner, Observer { grc.goToTime(it) })
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
