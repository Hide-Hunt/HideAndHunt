package ch.epfl.sdp.game

import android.os.Bundle
import android.widget.Toast
import ch.epfl.sdp.databinding.ActivityPreyBinding
import ch.epfl.sdp.game.data.*

/**
 * Activity shown as a prey during the game
 */
class PreyActivity : GameActivity() {
    private lateinit var binding: ActivityPreyBinding

    private lateinit var preyFragment: PreyFragment
    private lateinit var gameTimerFragment: GameTimerFragment
    private lateinit var predatorRadarFragment: PredatorRadarFragment

    val ranges: List<Int> = listOf(10, 20, 50, 100, 100000)
    val rangePopulation: HashMap<Int, Int> = HashMap()

    private var mostDangerousManDistance: Float = 10e+9f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (validGame) {
            for (p: Player in gameData.playerList.filterIsInstance<Predator>()) {
                locationHandler.subscribeToPlayer(p.id)
            }

            loadFragments()
        }
    }

    private fun loadFragments() {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()

        gameTimerFragment = GameTimerFragment.create(gameData.initialTime)
        fragmentTransaction.add(binding.frmTimerPrey.id, gameTimerFragment)

        preyFragment = PreyFragment.newInstance(ArrayList(players.values.filterIsInstance<Prey>().toList()))
        fragmentTransaction.add(binding.frmPreyRemaining.id, preyFragment)

        predatorRadarFragment = PredatorRadarFragment.newInstance(ArrayList(ranges))
        fragmentTransaction.add(binding.frmPredatorRadar.id, predatorRadarFragment)

        fragmentTransaction.commit()
    }

    private fun updateThreat() {
        resetRange()
        for (p in players.values) {
            if (p is Predator && p.lastKnownLocation != null) {
                val dist = p.lastKnownLocation!!.distanceTo(locationHandler.lastKnownLocation)
                if (dist < mostDangerousManDistance) {
                    mostDangerousManDistance = dist
                }
                for (i in ranges.indices) {
                    if (dist <= ranges[i]) {
                        rangePopulation[ranges[i]] = ((rangePopulation[ranges[i]]) ?: 0) + 1
                        break
                    }
                }
            }
        }

        predatorRadarFragment.updateInfos(mostDangerousManDistance, rangePopulation)
    }

    private fun resetRange() {
        mostDangerousManDistance = 10e+9f
        for (i in ranges.indices) {
            rangePopulation[ranges[i]] = 0
        }
    }

    override fun onLocationChanged(newLocation: Location) {
        players[gameData.playerID]?.lastKnownLocation = newLocation;
        updateThreat()
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

    override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
        super.onPlayerLocationUpdate(playerID, location)
        updateThreat()
    }

    override fun onPreyCatches(predatorID: Int, preyID: Int) {
        players[preyID]?.let {
            if (it is Prey && it.state == PreyState.ALIVE) {
                it.state = PreyState.DEAD
                preyFragment.setPreyState(preyID, PreyState.DEAD)
                Toast.makeText(this@PreyActivity, "Predator $predatorID caught prey $preyID", Toast.LENGTH_LONG).show()

                if (it.id == gameData.playerID) {
                    //I've been caught
                    EndGameHelper.startEndGameActivity(this, gameData.initialTime - gameTimerFragment.remaining, 0)
                    gameTimerFragment.stop()
                }
            }
        }
    }

    override fun onTimeOut() {
        EndGameHelper.startEndGameActivity(this, gameData.initialTime, 0)
    }
}
