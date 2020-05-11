package ch.epfl.sdp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.epfl.sdp.db.entities.ReplayDao
import ch.epfl.sdp.replay.ReplayInfo
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

typealias Callback<T> = (T) -> Unit
typealias UnitCallback = () -> Unit

@Database(entities = [ReplayInfo::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun replayDao(): ReplayDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        @InternalCoroutinesApi
        fun instance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            return synchronized(this) {
                val tempInstance2 = INSTANCE
                if (tempInstance2 != null) {
                    tempInstance2
                } else {
                    val instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "hide_hunt_database").build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
}