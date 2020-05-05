package ch.epfl.sdp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.databinding.ActivityMainBinding
import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.MockGlobalLobbyRepository
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val repositoryFactory = object : IRepoFactory {
        override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
            return MockGlobalLobbyRepository()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playButton.setOnClickListener {
            val intent = Intent(this@MainActivity, GlobalLobbyActivity::class.java)
            intent.putExtra("repoFactory", repositoryFactory)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener{
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.newGameButton.setOnClickListener{
            val intent = Intent(this@MainActivity, GameCreationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.playButton.isEnabled = LocalUser.connected
        binding.newGameButton.isEnabled = LocalUser.connected
    }
}