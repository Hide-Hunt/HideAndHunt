package ch.epfl.sdp.lobby.game

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.ActivityGameLobbyBinding
import ch.epfl.sdp.lobby.PlayerParametersFragment

class GameLobbyActivity : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener, PlayerParametersFragment.OnFactionChangeListener{
    private lateinit var mSwipeRefreshLayout : SwipeRefreshLayout
    private lateinit var rv : RecyclerView
    private val repository = MockGameLobbyRepository
    private lateinit var gameLobbyBinding: ActivityGameLobbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameLobbyBinding = ActivityGameLobbyBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_game_lobby)

        rv = findViewById<RecyclerView>(R.id.player_list)
        rv.layoutManager = LinearLayoutManager(this)

        repository.getPlayers { playerList -> rv.adapter = GameLobbyAdapter(playerList) }

        val gameInfo = findViewById<LinearLayout>(R.id.game_info)
        repository.getGameName { name ->
            val text = getText(R.string.game_name).toString() + " " + name
            (gameInfo.getChildAt(0) as TextView).text = text}
        repository.getGameDuration { game_duration ->
            val text = getText(R.string.game_duration).toString() + " " + game_duration + " minutes"
            (gameInfo.getChildAt(1) as TextView).text = text}

        supportFragmentManager.beginTransaction().add(R.id.faction_selection,PlayerParametersFragment()).commit()

        //SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.swipe_container)
        mSwipeRefreshLayout.setOnRefreshListener(this)

    }

    override fun onRefresh() {
        refreshPlayerList()
    }

    override fun onFactionChange(newFaction: PlayerParametersFragment.Faction) {
        //player id is hardcoded for now
        repository.setPlayerFaction(85,newFaction)
    }

    private fun refreshPlayerList() {
        mSwipeRefreshLayout.isRefreshing = true
        repository.getPlayers { playerList -> rv.adapter = GameLobbyAdapter(playerList) }
        mSwipeRefreshLayout.isRefreshing = false
    }
}