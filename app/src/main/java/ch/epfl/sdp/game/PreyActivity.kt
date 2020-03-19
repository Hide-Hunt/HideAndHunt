package ch.epfl.sdp.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityPreyBinding
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.game.data.Prey

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PreyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreyBinding
    private lateinit var preyFragment: PreyFragment
    private lateinit var gameTimerFragment: GameTimerFragment

    private var initialTime: Long = 0L
    private val players: HashMap<Int, Player> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialTime = intent.getLongExtra("initialTime", 2 * 60 * 1000)
        val playerList = (intent.getSerializableExtra("players") as List<*>).filterIsInstance<Player>()
        loadPlayers(playerList)
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

        fragmentTransaction.commit()
    }

}
