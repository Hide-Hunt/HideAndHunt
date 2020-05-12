package ch.epfl.sdp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.TypeConverters
import ch.epfl.sdp.replay.ReplayInfo
import kotlinx.coroutines.InternalCoroutinesApi


@Database(entities = [ReplayInfo::class], version = 1)
@TypeConverters(Converters::class)
abstract class FakeAppDatabase : AppDatabase() {

    companion object : AppDatabaseCompanion() {
        @Volatile private var INSTANCE: FakeAppDatabase? = null

        @InternalCoroutinesApi
        override fun instance(context: Context): FakeAppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            return kotlinx.coroutines.internal.synchronized(this) {
                val tempInstance2 = INSTANCE
                if (tempInstance2 != null) {
                    tempInstance2
                } else {
                    val instance = Room.inMemoryDatabaseBuilder(
                            context.applicationContext,
                            FakeAppDatabase::class.java).build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
}