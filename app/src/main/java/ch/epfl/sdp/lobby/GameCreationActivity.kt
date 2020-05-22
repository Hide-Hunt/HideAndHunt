package ch.epfl.sdp.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityGameCreationBinding
import ch.epfl.sdp.lobby.game.GameLobbyActivity
import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import javax.inject.Inject

/**
 * A simple [AppCompatActivity] to create a new game.
 * Redirect the user to [GameLobbyActivity] when the form is validated
 */
class GameCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameCreationBinding

    @Inject
    lateinit var lobbyRepo: IGameLobbyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        // We have to handle the dependency injection before the call to super.onCreate
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)


        super.onCreate(savedInstanceState)
        binding = ActivityGameCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {
            val time = binding.gameDuration.text.toString()
            val timeInSeconds: Long = time.toLong() * 60
            val newGameID = lobbyRepo.createGame(binding.gameName.text.toString(), timeInSeconds)
            val intent = Intent(this@GameCreationActivity, GameLobbyActivity::class.java)
            intent.putExtra("gameID", newGameID)
            startActivity(intent)
        }
    }
}
