package ch.epfl.sdp.replay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.ActivityManageReplaysBinding
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.replay.viewer.ReplayActivity
import java.io.File

class ManageReplaysActivity : AppCompatActivity(), ReplayInfoListFragment.OnListFragmentInteractionListener {
    private lateinit var binding: ActivityManageReplaysBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageReplaysBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repoFactory = object : IRepoFactory {
            override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
                TODO("Not yet implemented")
            }

            override fun makeReplayRepository(): IReplayRepository {
                return object : IReplayRepository {
                    override fun getAllGames(cb: Callback<List<ReplayInfo>>) {
                        cb(listOf(
                                ReplayInfo(0, 0, 2345, Faction.PREDATOR, "0.game"),
                                ReplayInfo(1, 6753759194, 6753759194+675, Faction.PREDATOR, "1.game"),
                                ReplayInfo(2, 964781131, 964781131+182, Faction.PREY, "2.game"),
                                ReplayInfo(3, 1982211276, 1982211276+871, Faction.PREDATOR, "3.game"),
                                ReplayInfo(4, 5893518155, 5893518155+139, Faction.PREY, "4.game"),
                                ReplayInfo(5, 8505536244, 8505536244+549, Faction.PREY, "5.game")
                        ))
                    }

                    override fun downloadReplay(gameID: Int, cb: Callback<File>) {
                        TODO("Not yet implemented")
                    }

                }
            }

        }
        val replayInfoList = ReplayInfoListFragment.newInstance(repoFactory)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(binding.replaysSelectionPlaceHolder.id, replayInfoList)
                    .commitNow()
        }
    }

    override fun onListFragmentInteraction(game: ReplayInfo) {
        val intent = Intent(this, ReplayActivity::class.java)
        intent.putExtra(ReplayActivity.REPLAY_PATH_ARG, game.localReplayPath)
        startActivity(intent)
    }
}
