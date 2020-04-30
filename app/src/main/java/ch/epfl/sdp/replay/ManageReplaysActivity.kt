package ch.epfl.sdp.replay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityManageReplaysBinding
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.replay.viewer.ReplayActivity
import javax.inject.Inject

class ManageReplaysActivity : AppCompatActivity(), ReplayInfoListFragment.OnListFragmentInteractionListener {
    private lateinit var binding: ActivityManageReplaysBinding
    @Inject lateinit var downloader: IReplayDownloader
    private lateinit var replayInfoListFragment: ReplayInfoListFragment
    private val downloads = ArrayList<IReplayDownloader.IReplayDownload>()

    val mockReplayList = listOf(
            ReplayInfo(0, 0, 2345, Faction.PREDATOR, true),
            ReplayInfo(1, 6753759194, 6753759194 + 675, Faction.PREDATOR, false),
            ReplayInfo(2, 964781131, 964781131 + 182, Faction.PREY, false),
            ReplayInfo(3, 1982211276, 1982211276 + 871, Faction.PREDATOR, false),
            ReplayInfo(4, 5893518155, 5893518155 + 139, Faction.PREY, false),
            ReplayInfo(5, 8505536244, 8505536244 + 549, Faction.PREY, false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityManageReplaysBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // downloader = FirebaseReplayDownloader(this)

        val repoFactory = object : IRepoFactory {
            override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
                TODO("Not yet implemented")
            }

            override fun makeReplayRepository(): IReplayRepository {
                return object : IReplayRepository {
                    override fun getAllGames(cb: Callback<List<ReplayInfo>>) {
                        cb(mockReplayList)
                    }
                }
            }

        }
        if (savedInstanceState == null) {
            replayInfoListFragment = ReplayInfoListFragment.newInstance(repoFactory)
            supportFragmentManager.beginTransaction()
                    .replace(binding.replaysSelectionPlaceHolder.id, replayInfoListFragment)
                    .commitNow()
        } else {
            replayInfoListFragment = supportFragmentManager.getFragment(savedInstanceState, "replayInfoListFragment") as ReplayInfoListFragment
        }
    }

    override fun onListFragmentInteraction(game: ReplayInfo) {
        if (game.localCopy) {
            val intent = Intent(this, ReplayActivity::class.java)
            intent.putExtra(ReplayActivity.REPLAY_PATH_ARG, "${game.gameID}.game")
            startActivity(intent)
        } else {
            AlertDialog.Builder(this)
                    .setTitle("No local copy")
                    .setMessage("Would you like to download the replay ?")
                    .setPositiveButton("Download") { dialog, i ->
                        downloader.download(game.gameID, {
                            replayInfoListFragment.setDownloadedGame(game.gameID)
                        }, { error ->
                            Toast.makeText(this, "Error downloading \"${game.gameID}\": $error", Toast.LENGTH_LONG).show()
                        })
                    }
                    .setNegativeButton("Cancel") {dialog, which ->  }
                    .create().show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        supportFragmentManager.putFragment(outState, "replayInfoListFragment", replayInfoListFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        downloads.forEach {
            it.cancel()
        }
    }
}
