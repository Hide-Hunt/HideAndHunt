package ch.epfl.sdp.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.FragmentPredatorRadarBinding
import kotlin.math.min

private const val ARG_RANGES = "ranges"

/**
 * A [Fragment] displaying the radar in the view for the predators
 */
class PredatorRadarFragment : Fragment() {

    private var _binding: FragmentPredatorRadarBinding? = null
    private val binding get() = _binding!!

    private lateinit var ranges: ArrayList<Int>

    fun updateInfos(mdm: Float, rangePopulation: HashMap<Int, Int>) {
        binding.txtClosestPredator.text = getString(R.string.closest_predator).format(mdm)

        var range = ranges.size - 1
        var count = 0
        for (r in 0 until ranges.size) {
            if (mdm < ranges[r]) {
                range = min(r + 1, ranges.size - 1)
                for (r2 in 0..range) {
                    count += rangePopulation[ranges[r2]] ?: 0
                }
                break
            }
        }

        binding.imgPredatorDistance.setImageResource(
                when (range - 1) {
                    0 -> R.drawable.ic_predator_distance_0
                    1 -> R.drawable.ic_predator_distance_1
                    2 -> R.drawable.ic_predator_distance_2
                    else -> R.drawable.ic_predator_distance_3
                }
        )

        binding.txtPredatorAround.text = getString(R.string.predator_around).format(count, ranges[range])
    }

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
