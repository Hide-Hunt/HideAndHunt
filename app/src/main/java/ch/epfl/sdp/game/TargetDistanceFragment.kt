package ch.epfl.sdp.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.epfl.sdp.R

import ch.epfl.sdp.databinding.FragmentTargetDistanceBinding

private const val ARG_RANGES = "ranges"

/**
 * A simple [Fragment] subclass.
 * Use the [TargetDistanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TargetDistanceFragment : Fragment() {
    private var _binding: FragmentTargetDistanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var ranges: ArrayList<Int>
    var distance = NO_DISTANCE
        set(value) {
            field = if (value < 0) { NO_DISTANCE } else { value }
            updateDistanceDisplay()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //ranges = it.getIntegerArrayList(ARG_RANGES)!!
            ranges = arrayListOf(0, 10, 25, 50, 100)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTargetDistanceBinding.inflate(inflater)

        updateDistanceDisplay()

        return binding.root
    }

    private fun updateDistanceDisplay() {
        if (distance != NO_DISTANCE) {
            val index = ranges.indexOfFirst { i -> i > distance }
            if (index != -1) {
                binding.distanceLabel.text = "%d - %d".format(ranges[index-1], ranges[index])
            } else {
                binding.distanceLabel.text = "%d+".format(ranges.last())
            }
        } else {
            binding.distanceLabel.text = getString(R.string.no_distance_label)
        }
    }

    companion object {
        const val NO_DISTANCE = -1

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param ranges Parameter 1.
         * @return A new instance of fragment TargetDistanceFragment.
         */
        @JvmStatic
        fun newInstance(ranges: ArrayList<Int>) =
                TargetDistanceFragment().apply {
                    arguments = Bundle().apply {
                        putIntegerArrayList(ARG_RANGES, ranges)
                    }
                }
    }
}
