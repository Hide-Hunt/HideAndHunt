package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.db.AppDatabase
import ch.epfl.sdp.db.AppDatabaseCompanion
import dagger.Module
import dagger.Provides

@Module
class DBModule {
    @Provides
    fun providesAppDatabaseCompanion(): AppDatabaseCompanion {
        return AppDatabase
    }
}