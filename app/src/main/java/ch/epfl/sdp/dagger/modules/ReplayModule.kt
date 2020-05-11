package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.replay.*
import dagger.Module
import dagger.Provides

@Module
class ReplayModule {
    @Provides
    fun providesFirebaseDownloader(): IReplayDownloader {
        return FirebaseReplayDownloader()
    }
}