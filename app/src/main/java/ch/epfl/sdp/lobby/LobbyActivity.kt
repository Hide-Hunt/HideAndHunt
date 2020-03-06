package ch.epfl.sdp.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.PredatorActivity
import ch.epfl.sdp.PreyActivity
import ch.epfl.sdp.databinding.ActivityLobbyBinding

class LobbyActivity : AppCompatActivity(), PlayerParametersFragment.OnFactionChangeListener {
    private lateinit var lobbyBinding: ActivityLobbyBinding
    private var myFaction: PlayerParametersFragment.Faction = PlayerParametersFragment.Faction.PREDATOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lobbyBinding = ActivityLobbyBinding.inflate(layoutInflater)

        setContentView(lobbyBinding.root)
        lobbyBinding.startGameButton.setOnClickListener {
            val intent = if (myFaction == PlayerParametersFragment.Faction.PREDATOR) {
                Intent(this@LobbyActivity, PredatorActivity::class.java)
            } else {
                Intent(this@LobbyActivity, PreyActivity::class.java)
            }
            startActivity(intent)
        }
    }

    override fun onFactionChange(newFaction: PlayerParametersFragment.Faction) {
        myFaction = newFaction
    }
}