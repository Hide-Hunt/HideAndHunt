package ch.epfl.sdp.lobby.game

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.TextView
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
import ch.epfl.sdp.game.*
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.IUserRepo
import javax.inject.Inject

/**
 * Game Lobby Activity showing the list of players and game info
 */
class GameLobbyActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
        PlayerParametersFragment.OnFactionChangeListener, IGameLobbyRepository.OnGameStartListener {

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rv: RecyclerView
    private lateinit var adminId: String

    @Inject
    lateinit var repository: IGameLobbyRepository

    @Inject
    lateinit var userRepo: IUserRepo
    private lateinit var gameID: String
    private val userID: String = LocalUser.uid
    private var playerID: Int = 0
    private lateinit var gameLobbyBinding: ActivityGameLobbyBinding
    private var myFaction: Faction = Faction.PREDATOR
    private var myTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        gameLobbyBinding = ActivityGameLobbyBinding.inflate(layoutInflater)
        setContentView(gameLobbyBinding.root)

        // TODO In case of error, start ErrorActivity
        gameID = intent.getStringExtra("gameID")!!

        rv = gameLobbyBinding.playerList
        rv.layoutManager = LinearLayoutManager(this)

        repository.addLocalParticipation(gameID, {
            //repository interactions
            repository.setPlayerFaction(gameID, userID, Faction.PREDATOR, {}, {
                // TODO handle error
            })
            repository.getAdminId(gameID, { id ->
                adminId = id
                repository.getParticipations(gameID, { playerList ->
                    rv.adapter = GameLobbyAdapter(playerList, userID, adminId, userRepo)
                }, {})
            }, {})
            repository.setOnGameStartListener(gameID, this)
        }, {
            // TODO handle error
        })

        //set game info views
        setGameLobbyViews()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            NFCTagHelper.intentToNFCTag(intent)?.let {
                myTag = it
                repository.setPlayerReady(gameID, userID, true, {
                    repository.setPlayerTag(gameID, userID, it, {
                        updateLocalPlayerState()
                    }, {
                        // TODO handle error
                    })
                }, {
                    // TODO handle error
                })
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

    override fun onFactionChange(newFaction: Faction) {
        //player id is hardcoded for now
        repository.setPlayerFaction(gameID, userID, newFaction, {
            myFaction = newFaction
            updateLocalPlayerState()
        }, {
            // TODO handle error
        })
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
        repository.setPlayerReady(gameID, userID, newReadyState, {
            refreshPlayerList()
        }, {
            // TODO handle error
        })
    }

    private fun refreshPlayerList(then: () -> Unit = {}) {
        repository.getParticipations(gameID, { playerList ->
            playerID = playerList.indexOfFirst { it.userID == userID }
            rv.adapter = GameLobbyAdapter(playerList, userID, adminId, userRepo)
            mSwipeRefreshLayout.isRefreshing = false
            then()
        }, {})
    }

    private fun setGameLobbyViews() {
        val gameInfo = gameLobbyBinding.gameInfo
        repository.getGameName(gameID, { name ->
            val text = getText(R.string.game_name).toString() + " " + name
            (gameInfo.getChildAt(0) as TextView).text = text
        }, {})
        repository.getGameDuration(gameID, { gameDuration ->
            val text = getText(R.string.game_duration).toString() + " " + gameDuration + " seconds"
            (gameInfo.getChildAt(1) as TextView).text = text
            supportFragmentManager.beginTransaction().add(R.id.faction_selection, PlayerParametersFragment()).commit()

            //SwipeRefreshLayout
            mSwipeRefreshLayout = gameLobbyBinding.swipeContainer
            mSwipeRefreshLayout.setOnRefreshListener(this)

            gameLobbyBinding.startButton.setOnClickListener {
                gameLobbyBinding.startButton.isEnabled = false
                repository.requestGameLaunch(gameID, {}, {
                    Toast.makeText(applicationContext, "Unable to start game", Toast.LENGTH_LONG).show()
                })
            }
        }, {})
        gameLobbyBinding.startButton.setBackgroundColor(Color.GREEN)
        gameLobbyBinding.leaveButton.setOnClickListener {
            finish() //Goes back to previous activity
        }
        gameLobbyBinding.leaveButton.setBackgroundColor(Color.RED)
    }

    override fun finish() {
        repository.setOnGameStartListener(gameID, null)
        //Remove player from lobby on finish
        repository.removeLocalParticipation(gameID, {}, {
            // TODO handle error
        })
        super.finish()
    }

    override fun onGameStart() {
        val intent = if (myFaction == Faction.PREDATOR) {
            Intent(this, PredatorActivity::class.java)
        } else {
            Intent(this, PreyActivity::class.java)
        }

        repository.getGameDuration(gameID, { gameDuration ->
            intent.putExtra("initialTime", gameDuration * 1000L)
            intent.putExtra("gameID", gameID)
            userRepo.addGameToHistory(LocalUser.uid, gameID)

            refreshPlayerList {
                intent.putExtra("playerID", playerID)
                //TODO: Fetch MQTT URI from somewhere ? and add to the intent
                repository.getPlayers(gameID, { pl ->
                    intent.putExtra("players", ArrayList(pl))
                    startActivity(intent)
                }, {})
            }
        }, {})
    }
}