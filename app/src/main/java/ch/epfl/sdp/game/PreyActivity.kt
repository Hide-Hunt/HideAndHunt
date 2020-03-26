package ch.epfl.sdp.game

import android.location.LocationListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityPreyBinding
import ch.epfl.sdp.game.data.*
import ch.epfl.sdp.game.location.ILocationListener
import ch.epfl.sdp.game.location.LocationHandler

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PreyActivity : AppCompatActivity(), ILocationListener {

    private lateinit var binding: ActivityPreyBinding
    private lateinit var preyFragment: PreyFragment
    private lateinit var gameTimerFragment: GameTimerFragment
    private lateinit var predatorRadarFragment: PredatorRadarFragment

    private lateinit var gameData: GameIntentUnpacker.GameIntentData
    private var validGame: Boolean = false

    val players: HashMap<Int, Player> = HashMap()
    val ranges: List<Int> = listOf(5, 10, 20, 50, Integer.MAX_VALUE)
    val rangePopulation: HashMap<Int, Int> = HashMap()
    private var mostDangerousManDistance: Float = Float.MAX_VALUE

    lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameDataAndValidity = GameIntentUnpacker.unpack(intent)
        validGame = gameDataAndValidity.second
        if(!validGame) {
            finish()
            return
        }
        gameData = gameDataAndValidity.first
        locationHandler = LocationHandler(this, this, gameData.gameID, gameData.playerID, gameData.mqttURI)
        loadPlayers(gameData.playerList)
        loadFragments()
    }

    private fun loadPlayers(lst: List<Player>) {
        for (p: Player in lst) {
            players[p.id] = p
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
        for(p in players.values) {
            if(p is Predator && p.lastKnownLocation != null) {
                val dist = p.lastKnownLocation!!.distanceTo(locationHandler.lastKnownLocation)
                if(dist < mostDangerousManDistance) {
                    mostDangerousManDistance = dist
                }
                for(i in ranges.indices - 1) {
                    if(dist <= ranges[i]) {
                        rangePopulation[ranges[i]] = ((rangePopulation[ranges[i]]) ?: 0) + 1
                        break
                    }
                }
            }
        }

        predatorRadarFragment.updateInfos(mostDangerousManDistance, rangePopulation)
    }

    private fun resetRange() {
        for(i in ranges.indices) {
            rangePopulation[ranges[i]] = 0
        }
    }

    override fun onLocationChanged(newLocation: Location) {
        updateThreat()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (validGame) {
            locationHandler.stop()
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
        players[playerID]?.lastKnownLocation = location
        updateThreat()
    }

    override fun onPreyCatches(predatorID: Int, preyID: Int) {
        players[preyID]?.let {
            if (it is Prey && it.state == PreyState.ALIVE) {
                it.state = PreyState.DEAD
                preyFragment.setPreyState(preyID, PreyState.DEAD)
                Toast.makeText(this@PreyActivity, "Predator $predatorID caught prey $preyID", Toast.LENGTH_LONG).show()
            }
        }
    }

}
