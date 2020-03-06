package ch.epfl.sdp.game

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    var distance = DISABLED
        set(value) {
            field = if (value < 0) { NO_DISTANCE } else { value }
            updateDistanceDisplay()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ranges = it.getIntegerArrayList(ARG_RANGES)!!
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
        if (distance >= 0) {
            val index = ranges.indexOfFirst { i -> i > distance }
            if (index != -1) {
                binding.distanceLabel.text = "%d - %d".format(ranges[index-1], ranges[index])
                binding.distanceImage.setImageResource(
                        when (index) {
                            1 -> R.drawable.ic_signal_4
                            2 -> R.drawable.ic_signal_3
                            3 -> R.drawable.ic_signal_2
                            4 -> R.drawable.ic_signal_1
                            else -> R.drawable.ic_signal_0
                        }
                )
            } else {
                binding.distanceLabel.text = "%d+".format(ranges.last())
                binding.distanceImage.setImageResource(R.drawable.ic_signal_0)
            }
        } else if (distance == NO_DISTANCE) {
            binding.distanceLabel.text = getString(R.string.no_distance_label)
            binding.distanceImage.setImageResource(R.drawable.no_signal_animation)
            val frameAnimation = binding.distanceImage.drawable as AnimationDrawable
            frameAnimation.start()
        } else {
            binding.distanceLabel.text = getString(R.string.tracking_disabled)
            binding.distanceImage.setImageDrawable(null)
        }
    }

    companion object {
        const val NO_DISTANCE = -1
        const val DISABLED = -2

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
