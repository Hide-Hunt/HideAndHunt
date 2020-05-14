package ch.epfl.sdp.game

/**
 * Static helper for the GameTimerFragment
 */
object TimerHelper {
    /**
     * Convert number of milliseconds to a formatted string "00:00"
     * @param millis Long: a number of milliseconds
     * @return String: a nicely formatted string
     */
    @JvmStatic
    fun millisToTimeString(millis: Long): String {
        val seconds = (millis / 1000).toInt() % 60
        val secondsString: String
        secondsString = if (seconds < 10) "0$seconds" else seconds.toString()
        return (millis.toInt() / 60000).toString() + ":" + secondsString
    }
}