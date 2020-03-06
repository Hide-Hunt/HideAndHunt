package ch.epfl.sdp.game

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.FragmentGameTimerBinding

class GameTimerFragment : Fragment() {
    interface GameTimeOutListener {
        fun onTimeOut()
    }

    private var _binding: FragmentGameTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var textView: TextView
    var listener: GameTimeOutListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGameTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = binding.currentTime
        arguments?.let {
            object : CountDownTimer(it.getLong(ARG_TIME), COUNTDOWN_INTERVAL.toLong()) {
                override fun onTick(millisUntilFinished: Long) {
                    textView.text = TimerHelper.millisToTimeString(millisUntilFinished)
                }

                override fun onFinish() {
                    textView.setText(R.string.game_over)
                    listener?.onTimeOut()
                }
            }.start()
        }

        if (arguments == null) {
            textView.setText(R.string.no_timer)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GameTimeOutListener) {
            listener = context
        }
    }

    companion object {
        private const val ARG_TIME = "time"
        private const val COUNTDOWN_INTERVAL = 1000
        @JvmStatic
        fun create(time: Long): GameTimerFragment {
            val fragment = GameTimerFragment()
            val args = Bundle()
            args.putLong(ARG_TIME, time)
            fragment.arguments = args
            return fragment
        }
    }
}