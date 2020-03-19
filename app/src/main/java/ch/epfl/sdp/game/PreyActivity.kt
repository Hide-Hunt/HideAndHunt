package ch.epfl.sdp.game

import android.location.LocationListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityPreyBinding
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.game.data.Prey
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

    private var initialTime: Long = 0L
    private val players: HashMap<Int, Player> = HashMap()

    private lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gameData = GameIntentUnpacker.unpack(intent)
        loadPlayers(gameData.playerList)
        loadFragments()
    }

    fun loadPlayers(lst: List<Player>) {
        for (p: Player in lst) {
            players[p.id] = p
        }
    }

    fun loadFragments() {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()

        gameTimerFragment = GameTimerFragment.create(initialTime)
        fragmentTransaction.add(binding.frmTimerPrey.id, gameTimerFragment)

        preyFragment = PreyFragment.newInstance(ArrayList(players.values.filterIsInstance<Prey>().toList()))
        fragmentTransaction.add(binding.frmPreyRemaining.id, preyFragment)

        predatorRadarFragment = PredatorRadarFragment.newInstance()

        fragmentTransaction.commit()
    }

    override fun onLocationChanged(newLocation: Location) {
        TODO("Not yet implemented")
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onProviderEnabled(provider: String) {
        TODO("Not yet implemented")
    }

    override fun onProviderDisabled(provider: String) {
        TODO("Not yet implemented")
    }

    override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
        TODO("Not yet implemented")
    }

    override fun onPreyCatches(predatorID: Int, preyID: Int) {
        TODO("Not yet implemented")
    }

}
