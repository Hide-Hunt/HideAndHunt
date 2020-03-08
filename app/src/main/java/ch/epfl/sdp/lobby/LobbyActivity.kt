package ch.epfl.sdp.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityLobbyBinding
import ch.epfl.sdp.game.PredatorActivity
import ch.epfl.sdp.game.PreyActivity
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import ch.epfl.sdp.game.data.PreyState

class LobbyActivity : AppCompatActivity(), PlayerParametersFragment.OnFactionChangeListener {
    private lateinit var lobbyBinding: ActivityLobbyBinding
    private var myFaction: PlayerParametersFragment.Faction = PlayerParametersFragment.Faction.PREDATOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lobbyBinding = ActivityLobbyBinding.inflate(layoutInflater)

        // Fake data for next activity
        val players = ArrayList<Player>()

        for (i in 0..19) {
            val p = if (Math.random() % 2 == 0.0) {
                Predator(i)
            } else {
                val p = Prey(i, i.toString())

                if (i >= 7) {
                    p.state = PreyState.DEAD
                }

                p
            }
            players.add(p)
        }
        players.add(Prey(24, "040D5702D36480"))
        players.add(Prey(42, "B2F55C01"))

        setContentView(lobbyBinding.root)
        lobbyBinding.startGameButton.setOnClickListener {
            val intent = if (myFaction == PlayerParametersFragment.Faction.PREDATOR) {
                Intent(this@LobbyActivity, PredatorActivity::class.java)
            } else {
                Intent(this@LobbyActivity, PreyActivity::class.java)
            }

            intent.putExtra("gameID", 0)
            intent.putExtra("playerID", 0)
            intent.putExtra("players", players)

            startActivity(intent)
        }
    }

    override fun onFactionChange(newFaction: PlayerParametersFragment.Faction) {
        myFaction = newFaction
    }
}