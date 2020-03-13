package ch.epfl.sdp.lobby.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.ActivityGameLobbyBinding

class GameLobbyActivity : AppCompatActivity()  {
    private val repository = MockGameLobbyRepository
    private lateinit var gameLobbyBinding: ActivityGameLobbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameLobbyBinding = ActivityGameLobbyBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_game_lobby)

        val rv = findViewById<RecyclerView>(R.id.player_list)
        rv.layoutManager = LinearLayoutManager(this)

        //caller id is hardcoded for now
        repository.getPlayers { playerList -> rv.adapter = GameLobbyAdapter(playerList,playerList[1].user.uid) }

    }
}