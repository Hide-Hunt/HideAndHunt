package ch.epfl.sdp.game;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentGameTimerBinding;

public class GameTimerFragment extends Fragment {

    public interface GameTimeOutListener {
        void onTimeOut();
    }


    private FragmentGameTimerBinding binding;

    private static final String ARG_TIME = "time";
    private static final int COUNTDOWN_INTERVAL = 1000;

    private TextView textView;
    public GameTimeOutListener listener;

    public GameTimerFragment() {}

    public static GameTimerFragment create(long time) {
        GameTimerFragment fragment = new GameTimerFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TIME, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGameTimerBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textView = binding.currentTime;

        if (getArguments() != null) {
            new CountDownTimer(getArguments().getLong(ARG_TIME), COUNTDOWN_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    textView.setText(millisToTimeString(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    if (listener != null) {
                        listener.onTimeOut();
                    }
                    else {
                        textView.setText(R.string.game_over);
                    }
                }
            }.start();
        } else {
            textView.setText(R.string.no_timer);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GameTimeOutListener) {
            this.listener = (GameTimeOutListener) context;
        }
    }

    private String millisToTimeString(long millis) {
        int seconds = (int) (millis / 1000) % 60;

        String secondsString;
        if (seconds < 10) secondsString = "0" + seconds;
        else secondsString = String.valueOf(seconds);

        return (int) millis / 60000 + ":" + secondsString;
    }
}
