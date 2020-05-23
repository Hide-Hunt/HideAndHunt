package ch.epfl.sdp.replay

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityManageReplaysBinding
import ch.epfl.sdp.replay.viewer.ReplayActivity
import javax.inject.Inject

class ManageReplaysActivity : AppCompatActivity(), ReplayInfoListFragment.OnListFragmentInteractionListener {
    private lateinit var binding: ActivityManageReplaysBinding

    @Inject
    lateinit var downloader: IReplayDownloader
    private lateinit var localReplayStore: LocalReplayStore
    private lateinit var replayInfoListFragment: ReplayInfoListFragment
    private val downloads = HashMap<String, IReplayDownloader.IReplayDownload>()


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityManageReplaysBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localReplayStore = LocalReplayStore(this)

        if (savedInstanceState == null) {
            replayInfoListFragment = ReplayInfoListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .replace(binding.replaysSelectionPlaceHolder.id, replayInfoListFragment)
                    .commitNow()
        } else {
            replayInfoListFragment = supportFragmentManager.getFragment(savedInstanceState, "replayInfoListFragment") as ReplayInfoListFragment
        }
    }

    /**
     * Reacts to an interaction with the ListFragment by either downloading the requested replay or
     * launching it
     * @param game The requested value selected by the user
     */
    override fun onListFragmentInteraction(game: ReplayInfo) {
        if (game.localCopy) {
            val intent = Intent(this, ReplayActivity::class.java)
            intent.putExtra(ReplayActivity.REPLAY_GAME_ID, game.gameID)
            startActivity(intent)
        } else {
            AlertDialog.Builder(this)
                    .setTitle("No local copy")
                    .setMessage("Would you like to download the replay ?")
                    .setPositiveButton("Download") { _, _ -> downloadReplay(game.gameID) }
                    .setNegativeButton("Cancel") { _, _ -> }
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
            it.value.cancel()
        }
    }

    /**
     * Downloads the requested replay
     * @param gameID The id of the game we want to download the replay
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun downloadReplay(gameID: String) {
        if (downloads.containsKey(gameID)) {
            Toast.makeText(this, "Already downloading replay", Toast.LENGTH_LONG).show()
            return
        }

        localReplayStore.createReplayDir()
        val tempFile = localReplayStore.getTmpFile(gameID)
        // TODO Set downloading state on replayInfoListFragment
        val dl = downloader.download(
                gameID,
                tempFile,
                {
                    tempFile.renameTo(localReplayStore.getFile(gameID))
                    replayInfoListFragment.setDownloadedGame(gameID)
                    downloads.remove(gameID)
                },
                { error ->
                    tempFile.delete()
                    downloads.remove(gameID)
                    // TODO Remove downloading state on replayInfoListFragment
                    Toast.makeText(this, "Error downloading \"${gameID}\": $error", Toast.LENGTH_LONG).show()
                }
        )
        downloads[gameID] = dl
    }
}
