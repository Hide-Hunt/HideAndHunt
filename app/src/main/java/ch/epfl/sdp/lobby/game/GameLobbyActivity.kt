package ch.epfl.sdp.lobby.game

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.epfl.sdp.MainActivity
import ch.epfl.sdp.PredatorActivity
import ch.epfl.sdp.PreyActivity
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.ActivityGameLobbyBinding
import ch.epfl.sdp.lobby.PlayerParametersFragment

class GameLobbyActivity : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener, PlayerParametersFragment.OnFactionChangeListener{

    //player id is hardcoded for now
    companion object {
        @JvmField val PLAYER_ID = 23
    }

    private lateinit var mSwipeRefreshLayout : SwipeRefreshLayout
    private lateinit var rv : RecyclerView
    private var adminId = 0
    private val repository = MockGameLobbyRepository
    private lateinit var gameLobbyBinding: ActivityGameLobbyBinding
    private var myFaction: PlayerParametersFragment.Faction = PlayerParametersFragment.Faction.PREDATOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameLobbyBinding = ActivityGameLobbyBinding.inflate(layoutInflater)
        setContentView(gameLobbyBinding.root)

        rv = findViewById(R.id.player_list)
        rv.layoutManager = LinearLayoutManager(this)

        repository.setPlayerFaction(PLAYER_ID,PlayerParametersFragment.Faction.PREDATOR)
        repository.getAdminId { id -> adminId = id }
        repository.getPlayers { playerList -> rv.adapter = GameLobbyAdapter(playerList, PLAYER_ID,adminId) }

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

        gameLobbyBinding.startButton.setOnClickListener {
            val intent = if (myFaction == PlayerParametersFragment.Faction.PREDATOR) {
                Intent(this, PredatorActivity::class.java)
            } else {
                Intent(this, PreyActivity::class.java)
            }
            startActivity(intent)
        }
        gameLobbyBinding.startButton.setBackgroundColor(Color.GREEN)

        gameLobbyBinding.leaveButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            startActivity(intent)
        }
        gameLobbyBinding.leaveButton.setBackgroundColor(Color.RED)

    }

    override fun onRefresh() {
        refreshPlayerList()
    }

    override fun onFactionChange(newFaction: PlayerParametersFragment.Faction) {
        //player id is hardcoded for now
        repository.setPlayerFaction(PLAYER_ID,newFaction)
        myFaction = newFaction
    }

    private fun refreshPlayerList() {
        mSwipeRefreshLayout.isRefreshing = true
        repository.getPlayers { playerList -> rv.adapter = GameLobbyAdapter(playerList, PLAYER_ID,adminId) }
        mSwipeRefreshLayout.isRefreshing = false
    }
}