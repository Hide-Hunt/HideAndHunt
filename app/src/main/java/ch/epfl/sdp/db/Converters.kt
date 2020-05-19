package ch.epfl.sdp.db

import androidx.room.TypeConverter
import ch.epfl.sdp.game.data.Faction

class Converters {
    @TypeConverter
    fun fromInt(value: Int?): Faction? {
        return if (value == null) null else Faction.values()[value]
    }

    @TypeConverter
    fun factionToInt(value: Faction?): Int? {
        return Faction.values().indexOf(value)
    }
}