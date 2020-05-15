package ch.epfl.sdp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.databinding.ActivityMainBinding
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import ch.epfl.sdp.replay.ManageReplaysActivity
import ch.epfl.sdp.user.ProfileActivity
import ch.epfl.sdp.user.UserCache

/**
 * Main app activity, where the user can start a game, create a game and login
 */
class MainActivity : AppCompatActivity() {

    val cache: UserCache = UserCache()
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

        buttonToActivity(binding.profileButton, ProfileActivity::class.java)

        buttonToActivity(binding.replayButton, ManageReplaysActivity::class.java)

        buttonToActivity(binding.newGameButton, GameCreationActivity::class.java)

        cache.get(this)
        activateProfile()
    }

    override fun onResume() {
        super.onResume()
        activateProfile()
    }

    private fun activateProfile() {
        if (!LocalUser.connected)
            binding.profileButton.visibility = View.INVISIBLE
        else
            binding.profileButton.visibility = View.VISIBLE
        binding.playButton.isEnabled = LocalUser.connected
        binding.newGameButton.isEnabled = LocalUser.connected
    }
}