package ch.epfl.sdp.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.epfl.sdp.databinding.FragmentPredatorRadarBinding

import ch.epfl.sdp.R

private const val ARG_RANGES = "ranges"

class PredatorRadarFragment : Fragment() {

    private var _binding: FragmentPredatorRadarBinding? = null
    private val binding get() = _binding!!

    private lateinit var ranges: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ranges = ArrayList((it.getSerializable(ARG_RANGES) as ArrayList<*>).filterIsInstance<Int>())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPredatorRadarBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun updateInfos(mdm: Float, rangePopulation: HashMap<Int, Int>) {
        binding.txtClosestPredator.text = getString(R.string.closest_predator).format(mdm)

        var range = 0
        var count = 0
        for(v in ranges) {
            if(v >= mdm) {
                range = v
                count = rangePopulation[v] ?: 0
                break
            }
        }

        binding.txtPredatorAround.text = getString(R.string.predator_around).format(range, count)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(ranges: ArrayList<Int>) =
                PredatorRadarFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_RANGES, ranges)
                    }
                }
    }
}
