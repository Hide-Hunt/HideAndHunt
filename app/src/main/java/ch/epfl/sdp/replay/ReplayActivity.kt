package ch.epfl.sdp.replay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.databinding.ActivityReplayBinding
import ch.epfl.sdp.game.data.*
import ch.epfl.sdp.replay.game_event.LocationEvent

class ReplayActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val p0 = Predator(0).also {
            it.lastKnownLocation = Location(46.16, 6.09)
        }
        val p1 = Prey(1).also {
            it.lastKnownLocation = Location(46.48, 6.27)
        }

        val history = GameHistory(0,
                listOf(p0, p1),
                Area(Location(46.16, 6.09), Location(46.48, 6.27)),
                listOf(
                        LocationEvent(0, 0, Location(46.236149566213456,6.1490024932050735)),
                        LocationEvent(1, 1, Location(46.23674933470297,6.147348814069256)),
                        LocationEvent(2, 0, Location(46.237283679042804,6.147942138543366)),
                        LocationEvent(3, 1, Location(46.23792309190521,6.147666453803363))
                )
        )

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(binding.replayControl.id, ReplayControlFragment.newInstance(ArrayList(history.event.map { it.timestamp })))
                    .replace(binding.replayMap.id, ReplayFragment.newInstance(history))
                    .commitNow()
        }
    }
}
