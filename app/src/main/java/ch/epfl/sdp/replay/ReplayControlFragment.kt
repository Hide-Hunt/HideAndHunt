package ch.epfl.sdp.replay

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.databinding.FragmentReplayControlBinding
import java.text.DateFormat
import java.util.Date
import kotlin.math.pow
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass.
 * Use the [ReplayControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReplayControlFragment : Fragment() {
    private var _binding: FragmentReplayControlBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReplayViewModel by activityViewModels()
    private var firstTimestamp by Delegates.notNull<Int>()
    private var lastTimestamp by Delegates.notNull<Int>()
    private val playHandler = Handler()
    private val playRunnable = Runnable { onPlayTick() }
    private var playSpeed: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            firstTimestamp = it.getInt(FIRST_TIMESTAMP)
            lastTimestamp = it.getInt(LAST_TIMESTAMP)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReplayControlBinding.inflate(inflater)

        timeSelectionAndDateDisplaySetup()
        buttonsAndSpeedSelectionSetup()

        return binding.root
    }

    private fun timeSelectionAndDateDisplaySetup() {
        val dateFormat = DateFormat.getDateTimeInstance()
        binding.date.text = dateFormat.format(Date(firstTimestamp.toLong() * 1000))

        binding.timeSelectionBar.max = lastTimestamp - firstTimestamp
        binding.timeSelectionBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val timestamp = firstTimestamp + progress
                binding.date.text = dateFormat.format(Date(timestamp.toLong() * 1000))
                viewModel.timeCursor.value = timestamp
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    private fun buttonsAndSpeedSelectionSetup() {
        binding.playButton.setOnClickListener { binding.speedSelection.progress = 1 }
        binding.stopButton.setOnClickListener { binding.speedSelection.progress = 0 }

        binding.speedSelection.progress = 1 // Set non-default value, so default will trigger listener

        binding.speedSelection.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                playSpeed = if (progress > 0) 2.0.pow(progress - 1.0).toInt() else 0
                binding.speedFactor.text = "x%d".format(playSpeed)
                onPlayTick()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        binding.speedSelection.progress = 0 //Real default value, will trigger listener
    }

    private fun onPlayTick() {
        if (playSpeed != 0) {
            binding.timeSelectionBar.progress++

            playHandler.removeCallbacks(playRunnable)
            playHandler.postDelayed(playRunnable, (1000.0 / playSpeed).toLong())
        }
    }

    companion object {
        private const val FIRST_TIMESTAMP = "first_timestamp"
        private const val LAST_TIMESTAMP = "last_timestamp"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param firstTimestamp First timestamp
         * @param lastTimestamp Last timestamp
         * @return A new instance of fragment ReplayControlFragment.
         */
        @JvmStatic
        fun newInstance(firstTimestamp: Int, lastTimestamp: Int) =
                ReplayControlFragment().apply {
                    arguments = Bundle().apply {
                        putInt(FIRST_TIMESTAMP, firstTimestamp)
                        putInt(LAST_TIMESTAMP, lastTimestamp)
                    }
                }
    }
}
