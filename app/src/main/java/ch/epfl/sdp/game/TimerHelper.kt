package ch.epfl.sdp.game

object TimerHelper {
    @JvmStatic
    fun millisToTimeString(millis: Long): String {
        val seconds = (millis / 1000).toInt() % 60
        val secondsString: String
        secondsString = if (seconds < 10) "0$seconds" else seconds.toString()
        return (millis.toInt() / 60000).toString() + ":" + secondsString
    }
}