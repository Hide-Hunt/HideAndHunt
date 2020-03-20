package ch.epfl.sdp.replay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.databinding.ReplayFragmentBinding
import kotlinx.android.synthetic.main.activity_prey.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint


class ReplayFragment : Fragment() {
    private var _binding: ReplayFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReplayViewModel by activityViewModels()
    private lateinit var history: GameHistory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = ReplayFragmentBinding.inflate(inflater)

        arguments?.let {
            history = it.getSerializable(ARG_HISTORY) as GameHistory
            val controller = binding.map.controller
            controller.setZoom(17.0)

            val center = viewModel.trackedPlayer.value?.let { playerID ->
                history.players[playerID].lastKnownLocation
            } ?: history.bounds.center

            viewModel.trackedPlayer.value

            controller.setCenter(GeoPoint(center.latitude, center.longitude))
        }



        return binding.root
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

        //46.317624332006794, 6.1874740646044
        fun newInstance(history: GameHistory) =
                ReplayFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_HISTORY, history)
                    }
                }
    }
}
