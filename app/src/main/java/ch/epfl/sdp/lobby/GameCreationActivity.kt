package ch.epfl.sdp.lobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.dagger.MyApplication
import ch.epfl.sdp.databinding.ActivityGameCreationBinding
import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import java.sql.Time
import javax.inject.Inject

class GameCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameCreationBinding
    @Inject lateinit var lobbyRepo: IGameLobbyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        // We have to handle the dependency injection before the call to super.onCreate
        (applicationContext as MyApplication).appComponent.inject(this)


        super.onCreate(savedInstanceState)
        binding = ActivityGameCreationBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.createButton.setOnClickListener {
            //val lobbyRepo = LobbyRepo(binding.gameName.text, binding.gameDuration.text)
            val time = binding.gameDuration.text.toString()
            val timInMilliseconds: Long = time.toLong() * 60000
            val newGameID = lobbyRepo.createGame(binding.gameName.text.toString(), Time(timInMilliseconds))
            val intent = Intent(this@GameCreationActivity, LobbyActivity::class.java)
            intent.putExtra("GameID", newGameID)
            startActivity(intent)
        }
    }
}
