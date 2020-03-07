package ch.epfl.sdp.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.game.PredatorActivity
import ch.epfl.sdp.game.PreyActivity
import ch.epfl.sdp.databinding.ActivityLobbyBinding
import ch.epfl.sdp.game.NFCTagHelper.byteArrayFromHexString
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Player

class LobbyActivity : AppCompatActivity(), PlayerParametersFragment.OnFactionChangeListener {
    private lateinit var lobbyBinding: ActivityLobbyBinding
    private var myFaction: PlayerParametersFragment.Faction = PlayerParametersFragment.Faction.PREDATOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lobbyBinding = ActivityLobbyBinding.inflate(layoutInflater)

        // Fake data for next activity
        val players = ArrayList<Player>()

        for (i in 0..9) {
            val faction = if (Math.random() % 2 == 0.0) Faction.PREDATOR else Faction.PREY
            players.add(Player(i, faction, i.toString()))
        }
        players.add(Player(24, Faction.PREY, "040D5702D36480"))
        players.add(Player(42, Faction.PREY, "B2F55C01"))

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