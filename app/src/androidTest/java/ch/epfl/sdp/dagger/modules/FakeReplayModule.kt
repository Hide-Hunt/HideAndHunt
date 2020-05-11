package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.replay.*
import dagger.Module
import dagger.Provides

@Module
class FakeReplayModule {
    @Provides
    fun providesFirebaseDownloader(): IReplayDownloader {
        return FakeReplayDownloader()
    }
}
