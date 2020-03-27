package ch.epfl.sdp.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityMainBinding
import ch.epfl.sdp.databinding.ActivityGameCreationBinding
import ch.epfl.sdp.lobby.game.GameLobbyActivity

class GameCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {
            val intent = Intent(this@GameCreationActivity, GameLobbyActivity::class.java)
            startActivity(intent)
        }
    }
}
