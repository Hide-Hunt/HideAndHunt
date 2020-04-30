package ch.epfl.sdp.lobby.game

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.epfl.sdp.R
import ch.epfl.sdp.authentication.User
import ch.epfl.sdp.databinding.ActivityGameLobbyBinding
import ch.epfl.sdp.game.NFCTagHelper
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.PredatorActivity
import ch.epfl.sdp.game.PreyActivity
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.lobby.PlayerParametersFragment
import javax.inject.Inject

/**
 * Game Lobby Activity showing the list of players and game info
 */
class GameLobbyActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, PlayerParametersFragment.OnFactionChangeListener {

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rv: RecyclerView
    @Inject lateinit var repository : IGameLobbyRepository;
    private lateinit var gameLobbyBinding: ActivityGameLobbyBinding
    private var myFaction: PlayerFaction = PlayerFaction.PREDATOR
    private var myTag: String? = null

    private var playerId: String = ""
    private var adminId: String  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        gameLobbyBinding = ActivityGameLobbyBinding.inflate(layoutInflater)
        setContentView(gameLobbyBinding.root)

        rv = gameLobbyBinding.playerList
        rv.layoutManager = LinearLayoutManager(this)

        //repository interactions
        playerId = if(!User.connected){
            repository.createPlayer(getString(R.string.default_username))
        }else{
            repository.addPlayer(User.uid, User.username)
        }

        repository.setPlayerFaction(playerId, PlayerFaction.PREDATOR)
        repository.getAdminId { id -> adminId = id }
        repository.getParticipations { playerList ->
            rv.adapter = GameLobbyAdapter(playerList, playerId, adminId)
        }

        //set game info views
        setGameLobbyViews()

    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            NFCTagHelper.intentToNFCTag(intent)?.let {
                myTag = it
                repository.setPlayerReady(playerId, true)
                repository.setPlayerTag(playerId, it)
                updateLocalPlayerState()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val adapter = NfcAdapter.getDefaultAdapter(this)
        adapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        NfcAdapter.getDefaultAdapter(this)?.disableForegroundDispatch(this)
    }

    override fun onRefresh() {
        refreshPlayerList()
    }

    override fun onBackPressed() {
        repository.removePlayer(playerId)
        super.onBackPressed()
    }

    override fun onFactionChange(newFaction: PlayerFaction) {
        //player id is hardcoded for now
        repository.setPlayerFaction(playerId, newFaction)
        myFaction = newFaction
        updateLocalPlayerState()
    }

    private fun updateLocalPlayerState() {
        if(myFaction == PlayerFaction.PREY && myTag == null) {
            gameLobbyBinding.txtPlayerReady.text = getString(R.string.you_are_not_ready)
            repository.setPlayerReady(playerId, false)
        } else {
            gameLobbyBinding.txtPlayerReady.text = getString(R.string.you_are_ready)
            repository.setPlayerReady(playerId, true)
        }
        refreshPlayerList()
    }

    private fun refreshPlayerList() {
        repository.getParticipations { playerList ->
            rv.adapter = GameLobbyAdapter(playerList, playerId, adminId)
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
            repository.removePlayer(playerId)
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
                intent.putExtra("playerID", playerId)
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