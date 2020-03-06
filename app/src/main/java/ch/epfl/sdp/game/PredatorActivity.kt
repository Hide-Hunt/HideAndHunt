package ch.epfl.sdp.game

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityPredatorBinding
import ch.epfl.sdp.game.TargetSelectionFragment.Companion.newInstance
import ch.epfl.sdp.game.TargetSelectionFragment.OnTargetSelectedListener
import java.util.*

class PredatorActivity : AppCompatActivity(), OnTargetSelectedListener {
    private var _binding: ActivityPredatorBinding? = null
    private val binding get() = _binding!!

    private var gameID = 0
    private var player: Player? = null
    private var targetID = 0
    private val lastKnownLocation: Location? = null
    private var players: ArrayList<Player>? = null
    private var targetSelectionFragment: TargetSelectionFragment? = null
    private var targetSDistanceFragment: TargetDistanceFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPredatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get game information
        if (savedInstanceState == null) { // First load
            loadIntentPayload(intent)
            loadFragments()
        } /* TODO get those information from bundle
        else { // Restore state from previous activity
            loadBundle(savedInstanceState)
        } */
    }

    private fun loadIntentPayload(intent: Intent) {
        // TODO get those information from intent
        fakeLoad()
    }

    /*
    private fun loadBundle(bundle: Bundle) {
    // TODO get those information from bundle
        fakeLoad()
    }
    */

    private fun loadFragments() { // create a FragmentManager
        val fm = supportFragmentManager
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()
        // replace the FrameLayout with new Fragment
        targetSelectionFragment = newInstance(players)
        fragmentTransaction.replace(binding.targetSelectionPlaceHolder.id, targetSelectionFragment!!)

        targetSDistanceFragment = TargetDistanceFragment.newInstance(arrayListOf(0,10,20,30,40))
        fragmentTransaction.replace(binding.targetDistancePlaceHolder.id, targetSDistanceFragment!!)

        fragmentTransaction.commit() // save the changes
    }

    private fun fakeLoad() {
        gameID = 0
        targetID = TargetSelectionFragment.NO_TARGET
        player = Player(0, Faction.PREDATOR)
        players = ArrayList()
        for (i in 1..9) {
            val faction = if (Math.random() % 2 == 0.0) Faction.PREDATOR else Faction.PREY
            players!!.add(Player(i, faction))
        }
    }

    override fun onTargetSelected(targetID: Int) {
        this.targetID = targetID
    }
}