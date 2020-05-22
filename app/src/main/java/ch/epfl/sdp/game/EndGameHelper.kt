package ch.epfl.sdp.game

import android.content.Context
import android.content.Intent

/**
 * Simple helper to start the end-game activity
 */
object EndGameHelper {

    /**
     * Start the end-game activity
     * @param ctx Context: The [Context] for the new activity
     * @param duration Long: The duration of the game
     * @param catchCount Int: The number of catch the player got
     */
    fun startEndGameActivity(ctx: Context, duration: Long, catchCount: Int) {
        val intent = Intent(ctx, EndGameActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("duration", duration)
        intent.putExtra("catchcount", catchCount)
        ctx.startActivity(intent)
    }
}