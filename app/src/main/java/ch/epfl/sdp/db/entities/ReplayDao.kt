package ch.epfl.sdp.db.entities

import androidx.room.*
import ch.epfl.sdp.replay.ReplayInfo

@Dao
interface ReplayDao {
    @Query("SELECT * FROM replays")
    fun getAll(): List<ReplayInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(replay: ReplayInfo)

    @Delete
    fun delete(replay: ReplayInfo)
}