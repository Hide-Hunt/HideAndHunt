package ch.epfl.sdp.game

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.error.ErrorActivity
import ch.epfl.sdp.error.ErrorCode
import ch.epfl.sdp.error.Error
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.game.data.Prey
import ch.epfl.sdp.game.location.ILocationListener
import ch.epfl.sdp.game.location.LocationHandler

/**
 *  Activity that shows the in-game predator interface
 */
abstract class GameActivity : AppCompatActivity(), ILocationListener, GameTimerFragment.GameTimeOutListener {
    protected lateinit var gameData: GameIntentUnpacker.GameIntentData
    protected var validGame: Boolean = false

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    var players = HashMap<Int, Player>()
    protected var preys = HashMap<String, Int>()

    private val heartbeatHandler = Handler()
    private val heartbeatRunnable = Runnable { onHeartbeat() }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get game information
        val gameDataAndValidity = GameIntentUnpacker.unpack(intent)
        validGame = gameDataAndValidity.second
        if(!validGame) {
            val error = Error(ErrorCode.INVALID_ACTIVITY_PARAMETER, "Invalid intent")
            ErrorActivity.startWith(this, error)
            finish()
            return
        }
        gameData = gameDataAndValidity.first
        locationHandler = LocationHandler(this, this, gameData.gameID, gameData.playerID, gameData.mqttURI)

        if (savedInstanceState == null) { // First load
            for (p in gameData.playerList) {
                players[p.id] = p
                if (p is Prey) {
                    preys[p.NFCTag] = p.id
                }
            }
        }

        onHeartbeat()
    }

    private fun onHeartbeat() {
        heartbeatHandler.postDelayed(heartbeatRunnable,1000)
        locationHandler.emitLocation()
    }

    override fun onResume() {
        super.onResume()
        locationHandler.enableRequestUpdates()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        locationHandler.onRequestPermissionsResult(requestCode)
    }

    override fun onPause() {
        super.onPause()
        locationHandler.removeUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (validGame) {
            heartbeatHandler.removeCallbacks(heartbeatRunnable)
            locationHandler.stop()
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Toast.makeText(applicationContext, "Location: onStatusChanged: $status", Toast.LENGTH_LONG).show()
    }

    override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
        players[playerID]?.lastKnownLocation = location
    }

    override fun onBackPressed() {
        //No code to avoid leaving the activity and returning to the game lobby
    }
}