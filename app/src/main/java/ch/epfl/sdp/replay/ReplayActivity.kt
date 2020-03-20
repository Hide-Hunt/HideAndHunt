package ch.epfl.sdp.replay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Area
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.replay.game_event.LocationEvent

class ReplayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.replay_activity)

        val history = GameHistory(0,
                listOf(Player(0), Player(1)),
                Area(Location(46.16, 6.09), Location(46.48, 6.27)),
                listOf(
                        LocationEvent(0, 0, Location(46.317624332006794, 6.1874740646044))
                )
        )

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ReplayFragment.newInstance(history))
                    .commitNow()
        }
    }
}
