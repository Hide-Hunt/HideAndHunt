package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.db.AppDatabaseCompanion
import ch.epfl.sdp.db.FakeAppDatabase
import ch.epfl.sdp.replay.FakeReplayDownloader
import ch.epfl.sdp.replay.IReplayDownloader
import dagger.Module
import dagger.Provides

@Module
class FakeReplayModule {
    @Provides
    fun providesFirebaseDownloader(): IReplayDownloader {
        return FakeReplayDownloader()
    }

    @Provides
    fun providesFakeAppDatabaseCompanion(): AppDatabaseCompanion {
        return FakeAppDatabase
    }
}
