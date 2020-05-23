package ch.epfl.sdp.dagger.modules

import android.content.Context
import ch.epfl.sdp.db.AppDatabaseCompanion
import ch.epfl.sdp.db.FakeAppDatabase
import ch.epfl.sdp.replay.FakeReplayDownloader
import ch.epfl.sdp.replay.IReplayDownloader
import dagger.Module
import dagger.Provides

@Module
class FakeReplayModule {
    @Provides
    fun providesFirebaseDownloader(context: Context): IReplayDownloader {
        return FakeReplayDownloader(context)
    }

    @Provides
    fun providesFakeAppDatabaseCompanion(): AppDatabaseCompanion {
        return FakeAppDatabase
    }
}
