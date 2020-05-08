package ch.epfl.sdp.dagger.modules

import android.content.Context
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.replay.FirebaseReplayDownloader
import ch.epfl.sdp.replay.IReplayDownloader
import ch.epfl.sdp.replay.IReplayRepository
import ch.epfl.sdp.replay.ReplayInfo
import dagger.Module
import dagger.Provides

@Module
class ReplayModule {
    @Provides
    fun providesFirebaseDownloader(context: Context): IReplayDownloader {
        return FirebaseReplayDownloader()
    }

    @Provides
    fun providesIReplayRepository(context: Context): IReplayRepository {

        val mockReplayList = listOf(
                ReplayInfo(0, 0, 2345, Faction.PREDATOR, true),
                ReplayInfo(1, 6753759194, 6753759194 + 675, Faction.PREDATOR, false),
                ReplayInfo(2, 964781131, 964781131 + 182, Faction.PREY, false),
                ReplayInfo(3, 1982211276, 1982211276 + 871, Faction.PREDATOR, false),
                ReplayInfo(4, 5893518155, 5893518155 + 139, Faction.PREY, false),
                ReplayInfo(5, 8505536244, 8505536244 + 549, Faction.PREY, false)
        )
        return object : IReplayRepository {
            override fun getAllGames(cb: Callback<List<ReplayInfo>>) {
                cb(mockReplayList)
            }
        }
    }
}