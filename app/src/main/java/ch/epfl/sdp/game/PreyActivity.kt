package ch.epfl.sdp.game

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
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
class PreyActivity : AppCompatActivity(), ILocationListener, GameTimerFragment.GameTimeOutListener {

    private lateinit var binding: ActivityPreyBinding
    private lateinit var preyFragment: PreyFragment
    private lateinit var gameTimerFragment: GameTimerFragment
    private lateinit var predatorRadarFragment: PredatorRadarFragment

    private lateinit var gameData: GameIntentUnpacker.GameIntentData
    private var validGame: Boolean = false

    val players: HashMap<Int, Player> = HashMap()
    val ranges: List<Int> = listOf(10, 20, 50, 100, 100000)
    val rangePopulation: HashMap<Int, Int> = HashMap()
    private var mostDangerousManDistance: Float = 10e+9f

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
            if(p is Predator) {
                locationHandler.subscribeToPlayer(p.id)
            }
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
                for(i in ranges.indices) {
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
        mostDangerousManDistance = 10e+9f
        for(i in ranges.indices) {
            rangePopulation[ranges[i]] = 0
        }
    }

    override fun onLocationChanged(newLocation: Location) {
        updateThreat()
    }

    override fun onResume() {
        super.onResume()
        locationHandler.enableRequestUpdates()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        locationHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        super.onPause()
        locationHandler.removeUpdates()
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

                if(it.id == gameData.playerID) {
                    //I've been caught
                    EndGameHelper.startEndGameActivity(this, gameData.initialTime - gameTimerFragment.remaining, 0)
                }
            }
        }
    }

    override fun onTimeOut() {
        EndGameHelper.startEndGameActivity(this, gameData.initialTime, 0)
    }

    override fun onBackPressed() {
        //No code
    }

}
