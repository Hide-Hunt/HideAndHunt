package ch.epfl.sdp.db.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ch.epfl.sdp.replay.ReplayInfo

@Dao
interface ReplayDao {
    @Query("SELECT * FROM replays")
    fun getAll(): List<ReplayInfo>

    @Insert
    fun insert(replay: ReplayInfo)

    @Delete
    fun delete(replay: ReplayInfo)
}