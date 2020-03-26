package ch.epfl.sdp.lobby.game

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.ActivityGameLobbyBinding
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.PredatorActivity
import ch.epfl.sdp.game.PreyActivity
import ch.epfl.sdp.lobby.PlayerParametersFragment

/**
 * Game Lobby Activity showing the list of players and game info
 */
class GameLobbyActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, PlayerParametersFragment.OnFactionChangeListener {

    //player id is hardcoded for now
    companion object {
        const val PLAYER_ID = 23
    }

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rv: RecyclerView
    private var adminId = 0
    private val repository = MockGameLobbyRepository
    private lateinit var gameLobbyBinding: ActivityGameLobbyBinding
    private var myFaction: PlayerFaction = PlayerFaction.PREDATOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameLobbyBinding = ActivityGameLobbyBinding.inflate(layoutInflater)
        setContentView(gameLobbyBinding.root)

        rv = gameLobbyBinding.playerList
        rv.layoutManager = LinearLayoutManager(this)

        //repository interactions
        repository.setPlayerFaction(PLAYER_ID, PlayerFaction.PREDATOR)
        repository.getAdminId { id -> adminId = id }
        repository.getParticipations { playerList ->
            rv.adapter = GameLobbyAdapter(playerList, PLAYER_ID, adminId)
        }

        //set game info views
        setGameLobbyViews()

    }

    override fun onRefresh() {
        refreshPlayerList()
    }

    override fun onFactionChange(newFaction: PlayerFaction) {
        //player id is hardcoded for now
        repository.setPlayerFaction(PLAYER_ID, newFaction)
        myFaction = newFaction
    }

    private fun refreshPlayerList() {
        repository.getParticipations { playerList ->
            rv.adapter = GameLobbyAdapter(playerList, PLAYER_ID, adminId)
            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setGameLobbyViews() {
        val gameInfo = gameLobbyBinding.gameInfo
        repository.getGameName { name ->
            val text = getText(R.string.game_name).toString() + " " + name
            (gameInfo.getChildAt(0) as TextView).text = text
        }
        repository.getGameDuration { gameDuration ->
            val text = getText(R.string.game_duration).toString() + " " + gameDuration + " seconds"
            (gameInfo.getChildAt(1) as TextView).text = text
            supportFragmentManager.beginTransaction().add(R.id.faction_selection, PlayerParametersFragment()).commit()

            //SwipeRefreshLayout
            mSwipeRefreshLayout = gameLobbyBinding.swipeContainer
            mSwipeRefreshLayout.setOnRefreshListener(this)

            //add intents to start button
            setIntent(gameDuration.toLong())
        }
        gameLobbyBinding.startButton.setBackgroundColor(Color.GREEN)
        gameLobbyBinding.leaveButton.setOnClickListener {
            finish() //Goes back to previous activity
        }
        gameLobbyBinding.leaveButton.setBackgroundColor(Color.RED)
    }

    private fun setIntent(gameDuration: Long) {
        gameLobbyBinding.startButton.setOnClickListener {
            val intent = if (myFaction == PlayerFaction.PREDATOR) {
                Intent(this, PredatorActivity::class.java)
            } else {
                Intent(this, PreyActivity::class.java)
            }

            repository.getGameId { gid ->
                intent.putExtra("initialTime", gameDuration * 1000L)
                intent.putExtra("playerID", 0) //TODO: User.uid.toLong(10) but what if uid is "" ?
                intent.putExtra("gameID", gid)
                //TODO: Fetch MQTT URI from somewhere ? and add to the intent
                repository.getPlayers { pl ->
                    intent.putExtra("players", ArrayList(pl))
                    startActivity(intent)
                }
            }
        }
    }
}