package ch.epfl.sdp.replay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.databinding.FragmentReplayControlBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ReplayControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReplayControlFragment : Fragment() {
    private var _binding: FragmentReplayControlBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReplayViewModel by activityViewModels()
    private lateinit var timeCodes: ArrayList<Int>
    private var playSpeed = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            timeCodes = it.getIntegerArrayList(ARG_TIME_CODES) as ArrayList<Int>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReplayControlBinding.inflate(inflater)

        binding.timeSelectionBar.max = timeCodes.size - 1
        binding.date.text = timeCodes.first().toString()
        binding.timeSelectionBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.date.text = timeCodes[progress].toString()
                viewModel.timeCursor.value = timeCodes[progress]
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.playButton.setOnClickListener { binding.speedSelection.progress = 1 }
        binding.stopButton.setOnClickListener { binding.speedSelection.progress = 0 }

        binding.speedSelection.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                playSpeed = progress
                binding.speedFactor.text = "x%d".format(playSpeed)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        return binding.root
    }

    companion object {
        private const val ARG_TIME_CODES = "time_codes"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param timeCodes Parameter 1.
         * @return A new instance of fragment ReplayControlFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(timeCodes: ArrayList<Int>) =
                ReplayControlFragment().apply {
                    arguments = Bundle().apply {
                        putIntegerArrayList(ARG_TIME_CODES, timeCodes)
                    }
                }
    }
}
