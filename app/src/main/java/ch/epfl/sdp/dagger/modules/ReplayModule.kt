package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.replay.FirebaseReplayDownloader
import ch.epfl.sdp.replay.IReplayDownloader
import dagger.Module
import dagger.Provides

@Module
class ReplayModule {
    @Provides
    fun providesFirebaseDownloader(): IReplayDownloader {
        return FirebaseReplayDownloader()
    }
}