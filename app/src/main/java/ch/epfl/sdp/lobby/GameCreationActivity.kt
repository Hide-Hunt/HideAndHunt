package ch.epfl.sdp.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.authentication.User
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityGameCreationBinding
import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import java.sql.Time
import javax.inject.Inject
import ch.epfl.sdp.lobby.game.GameLobbyActivity

class GameCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameCreationBinding
    @Inject lateinit var lobbyRepo: IGameLobbyRepository
    private val uid = User.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        // We have to handle the dependency injection before the call to super.onCreate
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)


        super.onCreate(savedInstanceState)
        binding = ActivityGameCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {
            val time = binding.gameDuration.text.toString()
            val timInMilliseconds: Long = time.toLong() * 60000
            val newGameID = lobbyRepo.createGame(binding.gameName.text.toString(), Time(timInMilliseconds), User.uid)
            val intent = Intent(this@GameCreationActivity, GameLobbyActivity::class.java)
            intent.putExtra("GameID", newGameID)
            startActivity(intent)
        }
    }
}
