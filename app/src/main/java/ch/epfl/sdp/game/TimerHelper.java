package ch.epfl.sdp.game;

public class TimerHelper {
    public static String millisToTimeString(long millis) {
        int seconds = (int) (millis / 1000) % 60;

        String secondsString;
        if (seconds < 10) secondsString = "0" + seconds;
        else secondsString = String.valueOf(seconds);

        return (int) millis / 60000 + ":" + secondsString;
    }
}
