package ch.epfl.sdp.lobby.game

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.epfl.sdp.R
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityGameLobbyBinding
import ch.epfl.sdp.db.SuccFailCallbacks.*
import ch.epfl.sdp.error.Error
import ch.epfl.sdp.error.ErrorActivity
import ch.epfl.sdp.error.ErrorCode
import ch.epfl.sdp.game.*
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.IUserRepo
import ch.epfl.sdp.utils.NFCHelper
import javax.inject.Inject

/**
 * Game Lobby Activity showing the list of players and game info
 */
class GameLobbyActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
        PlayerParametersFragment.OnFactionChangeListener, IGameLobbyRepository.OnGameStartListener {
    private lateinit var gameLobbyBinding: ActivityGameLobbyBinding

    @Inject lateinit var repository: IGameLobbyRepository
    @Inject lateinit var userRepo: IUserRepo
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var gameID: String
    private val userID: String = LocalUser.uid
    private var playerID: Int = 0
    private var adminId: String = ""
    private var myFaction: Faction = Faction.PREDATOR
    private var myTag: String? = null

    private fun joiningGameError() {
        val msg = "Unable to join lobby for game {}".format(gameID)
        ErrorActivity.startWith(this, Error(ErrorCode.OPERATION_FAILURE, msg))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)
        gameLobbyBinding = ActivityGameLobbyBinding.inflate(layoutInflater)
        setContentView(gameLobbyBinding.root)

        intent.getStringExtra("gameID").let {
            if (it == null) joiningGameError()
            else gameID = it
        }

        setLobbyDataFromRepo()
        setGameLobbyViews()
        setGameLobbyInteractiveElements()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            NFCTagHelper.intentToNFCTag(intent)?.let {
                myTag = it
                repository.setPlayerReady(gameID, userID, true, UnitSuccFailCallback({
                    repository.setPlayerTag(gameID, userID, it, UnitSuccFailCallback({
                        updateLocalPlayerState()
                    }))
                }))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        NFCHelper.enableForegroundDispatch(this, javaClass)
    }

    override fun onPause() {
        super.onPause()
        NFCHelper.disableForegroundDispatch(this)
    }

    override fun onRefresh() {
        refreshPlayerList()
    }

    override fun onFactionChange(newFaction: Faction) {
        //player id is hardcoded for now
        repository.setPlayerFaction(gameID, userID, newFaction, UnitSuccFailCallback({
            myFaction = newFaction
            updateLocalPlayerState()
        }))
    }

    private fun updateLocalPlayerState() {
        val newReadyState =
                if (myFaction == Faction.PREY && myTag == null) {
                    gameLobbyBinding.txtPlayerReady.text = getString(R.string.you_are_not_ready)
                    false
                } else {
                    gameLobbyBinding.txtPlayerReady.text = getString(R.string.you_are_ready)
                    true
                }

        repository.setPlayerReady(gameID, userID, newReadyState, UnitSuccFailCallback({
            refreshPlayerList()
        }))
    }

    private fun refreshPlayerList(then: () -> Unit = {}) {
        repository.getParticipation(gameID, SuccFailCallback({ playerList ->
            playerID = playerList.indexOfFirst { it.userID == userID }
            recyclerView.adapter = GameLobbyAdapter(playerList, userID, adminId, userRepo)
            mSwipeRefreshLayout.isRefreshing = false
            then()
        }))
    }

    private fun setLobbyDataFromRepo() {
        recyclerView = gameLobbyBinding.playerList
        recyclerView.layoutManager = LinearLayoutManager(this)

        repository.addLocalParticipation(gameID, UnitSuccFailCallback({
            repository.setOnGameStartListener(gameID, this)
        }, { joiningGameError() }))

        repository.getAdminId(gameID, SuccFailCallback({ id ->
            adminId = id
            repository.getParticipation(gameID, SuccFailCallback({ playerList ->
                recyclerView.adapter = GameLobbyAdapter(playerList, userID, adminId, userRepo)
            }))
        }))
    }

    private fun setGameLobbyViews() {
        repository.getGameName(gameID, SuccFailCallback({ name ->
            gameLobbyBinding.gameName.text = getString(R.string.game_name).format(name)
        }))
        repository.getGameDuration(gameID, SuccFailCallback({ gameDuration ->
            gameLobbyBinding.gameDuration.text = getString(R.string.game_duration).format(gameDuration)
        }))
        supportFragmentManager.beginTransaction().add(R.id.faction_selection, PlayerParametersFragment()).commit()
    }

    private fun setGameLobbyInteractiveElements() {
        //SwipeRefreshLayout
        mSwipeRefreshLayout = gameLobbyBinding.swipeContainer
        mSwipeRefreshLayout.setOnRefreshListener(this)

        gameLobbyBinding.startButton.setOnClickListener {
            gameLobbyBinding.startButton.isEnabled = false
            repository.requestGameLaunch(gameID, UnitSuccFailCallback({}, {
                this@GameLobbyActivity.gameLobbyBinding.startButton.isEnabled = true
                Toast.makeText(applicationContext, "Unable to start game", Toast.LENGTH_LONG).show()
            }))
        }

        gameLobbyBinding.leaveButton.setOnClickListener {
            finish() //Goes back to previous activity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.setOnGameStartListener(gameID, null)
    }

    override fun finish() {
        super.finish()
        //Remove player from lobby on finish
        repository.removeLocalParticipation(gameID, UnitSuccFailCallback())
    }

    override fun onGameStart() {
        val intent = if (myFaction == Faction.PREDATOR) {
            Intent(this, PredatorActivity::class.java)
        } else {
            Intent(this, PreyActivity::class.java)
        }

        repository.getGameDuration(gameID, SuccFailCallback({ gameDuration ->
            intent.putExtra("initialTime", gameDuration * 1000L)
            intent.putExtra("gameID", gameID)
            userRepo.addGameToHistory(LocalUser.uid, gameID)

            refreshPlayerList {
                intent.putExtra("playerID", playerID)
                //TODO: Fetch MQTT URI from somewhere ? and add to the intent
                repository.getPlayers(gameID, SuccFailCallback({ pl ->
                    intent.putExtra("players", ArrayList(pl))
                    startActivity(intent)
                }))
            }
        }))
    }
}