package ch.epfl.sdp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityMainBinding
import ch.epfl.sdp.lobby.LobbyActivity
import ch.epfl.sdp.authentication.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LobbyActivity::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener{
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}