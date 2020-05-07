package ch.epfl.sdp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.authentication.User
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityMainBinding
import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.MockGlobalLobbyRepository
import ch.epfl.sdp.user.ProfileActivity
import ch.epfl.sdp.user.UserCache


class MainActivity : AppCompatActivity() {
    val cache: UserCache = UserCache()
    private lateinit var binding: ActivityMainBinding
    private val repositoryFactory = object : IRepoFactory {
        override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
            return MockGlobalLobbyRepository()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // We have to handle the dependency injection before the call to super.onCreate
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)
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
        binding.profileButton.setOnClickListener{
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.newGameButton.setOnClickListener{
            val intent = Intent(this@MainActivity, GameCreationActivity::class.java)
            startActivity(intent)
        }
        binding.btnDebug.setOnClickListener {
            startActivity(Intent(this@MainActivity, DebugActivity::class.java))
        }
        cache.get(this)
        activateProfile()
    }

    override fun onRestart() {
        super.onRestart()
        activateProfile()
    }

    override fun onResume() {
        super.onResume()
        activateProfile()
    }

    private fun activateProfile() {
        if(!User.connected)
            binding.profileButton.visibility = View.INVISIBLE
        else
            binding.profileButton.visibility = View.VISIBLE
    }
}