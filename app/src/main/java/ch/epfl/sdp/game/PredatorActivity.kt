package ch.epfl.sdp.game

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityPredatorBinding
import ch.epfl.sdp.error.ErrorActivity
import ch.epfl.sdp.error.ErrorCode
import ch.epfl.sdp.error.Error
import ch.epfl.sdp.game.TargetSelectionFragment.OnTargetSelectedListener
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.game.data.Prey
import ch.epfl.sdp.game.data.PreyState
import ch.epfl.sdp.game.location.ILocationListener
import ch.epfl.sdp.game.location.LocationHandler

/**
 *  Activity that shows the in-game predator interface
 */
class PredatorActivity : AppCompatActivity(), OnTargetSelectedListener, ILocationListener, GameTimerFragment.GameTimeOutListener {
    private lateinit var binding: ActivityPredatorBinding

    private lateinit var gameData: GameIntentUnpacker.GameIntentData
    private var validGame: Boolean = false
    private var targetID: Int = TargetSelectionFragment.NO_TARGET

    private var players = HashMap<Int, Player>()
    private var preys = HashMap<String, Int>()

    private lateinit var gameTimerFragment: GameTimerFragment
    private lateinit var targetSelectionFragment: TargetSelectionFragment
    private lateinit var targetDistanceFragment: TargetDistanceFragment
    private lateinit var preyFragment: PreyFragment
    private val heartbeatHandler = Handler()
    private val heartbeatRunnable = Runnable { onHeartbeat() }

    private var catchCount = 0

    private lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

        loadFragments()
        onHeartbeat()
    }

    private fun onHeartbeat() {
        heartbeatHandler.postDelayed(heartbeatRunnable,1000)
        locationHandler.emitLocation()
    }

    private fun loadFragments() { // create a FragmentManager
        val fm = supportFragmentManager
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()

        // replace the FrameLayout with new Fragment
        gameTimerFragment = GameTimerFragment.create(gameData.initialTime)
        fragmentTransaction.add(binding.gameTimerPlaceHolder.id, gameTimerFragment)

        targetSelectionFragment = TargetSelectionFragment.newInstance(ArrayList(players.values.filterIsInstance<Prey>().toList()))
        fragmentTransaction.add(binding.targetSelectionPlaceHolder.id, targetSelectionFragment)

        targetDistanceFragment = TargetDistanceFragment.newInstance(arrayListOf(0, 10, 25, 50, 75))
        fragmentTransaction.add(binding.targetDistancePlaceHolder.id, targetDistanceFragment)

        preyFragment = PreyFragment.newInstance(ArrayList(players.values.filterIsInstance<Prey>().toList()))
        fragmentTransaction.add(binding.preysPlaceHolder.id, preyFragment)

        fragmentTransaction.commit() // save the changes
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val adapter = NfcAdapter.getDefaultAdapter(this)
        adapter?.enableForegroundDispatch(this, pendingIntent, null, null)
        locationHandler.enableRequestUpdates()
    }

    override fun onTargetSelected(targetID: Int) {
        if (this.targetID != TargetSelectionFragment.NO_TARGET) {
            locationHandler.unsubscribeFromPlayer(this.targetID)
        }
        this.targetID = targetID
        if (targetID != TargetSelectionFragment.NO_TARGET) {
            players[targetID]?.lastKnownLocation = null
            targetDistanceFragment.distance = TargetDistanceFragment.NO_DISTANCE
            locationHandler.subscribeToPlayer(this.targetID)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            NFCTagHelper.intentToNFCTag(intent)?.let {
                preys[it]?.let { preyID ->
                    onPreyCatches(gameData.playerID, preyID)
                    locationHandler.declareCatch(preyID)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        locationHandler.onRequestPermissionsResult(requestCode)
    }

    override fun onPause() {
        super.onPause()
        locationHandler.removeUpdates()
        NfcAdapter.getDefaultAdapter(this)?.disableForegroundDispatch(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (validGame) {
            heartbeatHandler.removeCallbacks(heartbeatRunnable)
            locationHandler.stop()
        }
    }

    override fun onLocationChanged(newLocation: Location) {
        if (targetID != TargetSelectionFragment.NO_TARGET) {
            targetDistanceFragment.distance =
                    players[targetID]?.lastKnownLocation?.distanceTo(newLocation)?.toInt()
                            ?: TargetDistanceFragment.NO_DISTANCE
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Toast.makeText(applicationContext, "Location: onStatusChanged: $status", Toast.LENGTH_LONG).show()
    }

    override fun onProviderEnabled(provider: String) {
        if (targetID != TargetSelectionFragment.NO_TARGET) {
            targetDistanceFragment.distance = TargetDistanceFragment.NO_DISTANCE
        }
    }

    override fun onProviderDisabled(provider: String) {
        targetDistanceFragment.distance = TargetDistanceFragment.DISABLED
    }

    override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
        if (players.containsKey(playerID)) {
            players[playerID]!!.lastKnownLocation = location
            if (playerID == targetID) {
                targetDistanceFragment.distance =
                        players[targetID]?.lastKnownLocation?.distanceTo(locationHandler.lastKnownLocation)?.toInt()
                                ?: TargetDistanceFragment.NO_DISTANCE
            }
        }
    }

    override fun onPreyCatches(predatorID: Int, preyID: Int) {
        players[preyID]?.let {
            if (it is Prey && it.state == PreyState.ALIVE) {
                it.state = PreyState.DEAD
                preyFragment.setPreyState(preyID, PreyState.DEAD)
            }
        }

        if (players.values.filterIsInstance<Prey>().none { p -> p.state != PreyState.DEAD }) {
            EndGameHelper.startEndGameActivity(this, gameData.initialTime - gameTimerFragment.remaining, 0)
        }
    }

    override fun onTimeOut() {
        EndGameHelper.startEndGameActivity(this, gameData.initialTime, catchCount)
    }

    override fun onBackPressed() {
        //No code to avoid leaving the activity and returning to the game lobby
    }
}