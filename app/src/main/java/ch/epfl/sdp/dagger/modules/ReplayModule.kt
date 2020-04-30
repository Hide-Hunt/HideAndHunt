package ch.epfl.sdp.dagger.modules

import android.content.Context
import ch.epfl.sdp.replay.FirebaseReplayDownloader
import ch.epfl.sdp.replay.IReplayDownloader
import dagger.Module
import dagger.Provides

@Module
class ReplayModule {
    @Provides
    fun providesFirebaseDownloader(context: Context): IReplayDownloader {
        return FirebaseReplayDownloader(context)
    }
}