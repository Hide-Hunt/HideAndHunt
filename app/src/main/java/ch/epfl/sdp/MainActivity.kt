package ch.epfl.sdp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.databinding.ActivityMainBinding
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.MockGlobalLobbyRepository
import ch.epfl.sdp.replay.IReplayRepository
import ch.epfl.sdp.replay.ManageReplaysActivity
import ch.epfl.sdp.replay.viewer.ReplayActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private fun buttonToActivity(button: Button, cls: Class<*>, intentFiller: (Intent) -> Unit = {}) {
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, cls)
            intentFiller(intent)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonToActivity(binding.playButton, GlobalLobbyActivity::class.java)

        buttonToActivity(binding.loginButton, LoginActivity::class.java)

        buttonToActivity(binding.replayButton, ManageReplaysActivity::class.java) {
            it.putExtra(ReplayActivity.REPLAY_PATH_ARG, "0.game")
        }

        buttonToActivity(binding.newGameButton, GameCreationActivity::class.java)


        //buttonToActivity(binding.btnDebug, DebugActivity::class.java)

        buttonToActivity(binding.loginButton, LoginActivity::class.java)

    }

    override fun onResume() {
        super.onResume()
        binding.playButton.isEnabled = LocalUser.connected
        binding.newGameButton.isEnabled = LocalUser.connected
    }
}