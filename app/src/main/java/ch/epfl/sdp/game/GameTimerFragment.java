package ch.epfl.sdp.game;

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

public class GameTimerFragment extends Fragment {

    private static final String ARG_TIME = "time";
    private static final int COUNTDOWN_INTERVAL = 1000;

    private TextView textView;

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
        return inflater.inflate(R.layout.fragment_game_timer,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView = view.findViewById(R.id.currentTime);
        if (getArguments() != null) {
            new CountDownTimer(getArguments().getLong(ARG_TIME), COUNTDOWN_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    String secondsString;
                    if (seconds < 10) secondsString = "0" + seconds;
                    else secondsString = String.valueOf(seconds);
                    String timeLeft = (int) millisUntilFinished / 60000 + ":" + secondsString;
                    textView.setText(timeLeft);
                }

                @Override
                public void onFinish() {
                    String endText = "GAME OVER !";
                    textView.setText(endText);
                }
            }.start();
        }
    }
}
