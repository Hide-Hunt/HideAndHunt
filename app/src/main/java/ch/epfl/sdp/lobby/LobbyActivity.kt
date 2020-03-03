package ch.epfl.sdp.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.PredatorActivity
import ch.epfl.sdp.databinding.ActivityLobbyBinding

class LobbyActivity : AppCompatActivity() {
    private lateinit var lobbyBinding: ActivityLobbyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lobbyBinding = ActivityLobbyBinding.inflate(layoutInflater)

        setContentView(lobbyBinding.root)
        lobbyBinding.startGameButton.setOnClickListener {
            val intent = Intent(this@LobbyActivity, PredatorActivity::class.java)
            startActivity(intent)
        }
    }
}