package ch.epfl.sdp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.databinding.ActivityMainBinding
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import ch.epfl.sdp.user.ProfileActivity
import ch.epfl.sdp.user.UserCache


class MainActivity : AppCompatActivity() {
    val cache: UserCache = UserCache()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playButton.setOnClickListener {
            val intent = Intent(this@MainActivity, GlobalLobbyActivity::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener{
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.profileButton.setOnClickListener{
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.newGameButton.setOnClickListener{
            val intent = Intent(this@MainActivity, GameCreationActivity::class.java)
            startActivity(intent)
        }
        cache.get(this)
        activateProfile()
    }

    override fun onResume() {
        super.onResume()
        activateProfile()
    }

    private fun activateProfile() {
        if(!LocalUser.connected)
            binding.profileButton.visibility = View.INVISIBLE
        else
            binding.profileButton.visibility = View.VISIBLE
        binding.playButton.isEnabled = LocalUser.connected
        binding.newGameButton.isEnabled = LocalUser.connected
    }
}