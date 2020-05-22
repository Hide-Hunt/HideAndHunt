package ch.epfl.sdp.game

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import ch.epfl.sdp.databinding.ActivityPredatorBinding
import ch.epfl.sdp.game.TargetSelectionFragment.OnTargetSelectedListener
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Prey
import ch.epfl.sdp.game.data.PreyState

/**
 *  Activity that shows the in-game predator interface
 */
class PredatorActivity : GameActivity(), OnTargetSelectedListener {
    private lateinit var binding: ActivityPredatorBinding

    private var targetID: Int = TargetSelectionFragment.NO_TARGET

    private lateinit var gameTimerFragment: GameTimerFragment
    private lateinit var targetSelectionFragment: TargetSelectionFragment
    private lateinit var targetDistanceFragment: TargetDistanceFragment
    private lateinit var preyFragment: PreyFragment

    private var catchCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragments()
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
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            NFCTagHelper.intentToNFCTag(intent)?.let {
                preys[it]?.let { preyID ->
                    catchCount++
                    onPreyCatches(gameData.playerID, preyID)
                    locationHandler.declareCatch(preyID)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        NfcAdapter.getDefaultAdapter(this)?.disableForegroundDispatch(this)
    }

    override fun onLocationChanged(newLocation: Location) {
        if (targetID != TargetSelectionFragment.NO_TARGET) {
            targetDistanceFragment.distance =
                    players[targetID]?.lastKnownLocation?.distanceTo(newLocation)?.toInt()
                            ?: TargetDistanceFragment.NO_DISTANCE
        }
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
        if (players.containsKey(playerID) && playerID == targetID) {
            targetDistanceFragment.distance =
                    players[targetID]?.lastKnownLocation?.distanceTo(locationHandler.lastKnownLocation)?.toInt()
                            ?: TargetDistanceFragment.NO_DISTANCE
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
            gameTimerFragment.stop()
        }
    }

    override fun onTimeOut() {
        EndGameHelper.startEndGameActivity(this, gameData.initialTime, catchCount)
    }
}