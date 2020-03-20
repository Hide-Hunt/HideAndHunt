package ch.epfl.sdp.replay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ch.epfl.sdp.databinding.FragmentReplayBinding
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.replay.game_event.CatchEvent
import ch.epfl.sdp.replay.game_event.LocationEvent
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class ReplayFragment : Fragment() {
    private var _binding: FragmentReplayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReplayViewModel by activityViewModels()
    private lateinit var history: GameHistory
    private val playerOverlays = ArrayList<Marker>()

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
        val controller = binding.map.controller
        controller.setZoom(15.0)

        val center = viewModel.trackedPlayer.value?.let { playerID ->
            history.players[playerID].lastKnownLocation
        } ?: history.bounds.center

        viewModel.trackedPlayer.value

        controller.setCenter(GeoPoint(center.latitude, center.longitude))

        for (player in history.players) {
            val loc = player.lastKnownLocation ?: history.bounds.center
            playerOverlays.add(Marker(binding.map).also {
                it.position = GeoPoint(loc.latitude, loc.longitude)
                it.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                binding.map.overlays.add(it)
            })
        }

        binding.map.overlayManager.addAll(playerOverlays)
        val commands = ArrayList<ReplayStep>().also {
            val lastPos = HashMap<Int, Location>()
            for (event in history.event) {
                it.add(when (event) {
                    is LocationEvent -> object : ReplayStep {
                        override fun execute() {
                            playerOverlays[event.playerID].position = GeoPoint(event.location.latitude, event.location.longitude)
                            lastPos[event.playerID] = event.location
                        }

                        override fun undo() {
                            lastPos[event.playerID]?.let {loc ->
                                playerOverlays[event.playerID].position = GeoPoint(loc.latitude, loc.longitude)
                                binding.map.invalidate()
                            }
                        }
                    }

                    is CatchEvent -> object : ReplayStep {
                        override fun execute() {
                            TODO("Not yet implemented")
                        }

                        override fun undo() {
                            TODO("Not yet implemented")
                        }
                    }

                    else -> ReplayStep.NOP
                })
            }
        }

        val grc = GameReplayController(commands)
        viewModel.timeCursor.observe(viewLifecycleOwner, Observer { grc.goToTime(it) })
        binding.map.setMultiTouchControls(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        Configuration.getInstance().load(context, prefs)
    }

    override fun onResume() {
        super.onResume()

        try {
            val tileSource: ITileSource = TileSourceFactory.getTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE.name())
            binding.map.setTileSource(tileSource)
        } catch (e: IllegalArgumentException) {
            binding.map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        }

        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    companion object {
        private const val PREFS_NAME = "ch.epfl.sdp.replay.prefs"
        private const val ARG_HISTORY = "history"

        fun newInstance(history: GameHistory) =
                ReplayFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_HISTORY, history)
                    }
                }
    }
}
