package ch.epfl.sdp.game

import android.content.Context
import android.content.Intent

object EndGameHelper {

    fun startEndGameActivity(ctx: Context, duration: Long, catchCount: Int) {
        val intent = Intent(ctx, EndGameActivity::class.java)
        intent.putExtra("duration", duration)
        intent.putExtra("catchcount", catchCount)
        ctx.startActivity(intent)
    }
}