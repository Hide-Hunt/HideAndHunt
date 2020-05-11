package ch.epfl.sdp.replay

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ch.epfl.sdp.game.data.Faction

@Entity(tableName = "replays")
data class ReplayInfo(
        @PrimaryKey var gameID: String,
        var name: String,
        var startTimestamp: Long,
        var endTimestamp: Long,
        var score: String,
        var winningFaction: Faction,
        @Ignore var localCopy: Boolean
) {
    // For room construction
    constructor(gameID: String,
                name: String,
                startTimestamp: Long,
                endTimestamp: Long,
                score: String,
                winningFaction: Faction) :
            this(gameID, name, startTimestamp, endTimestamp, score, winningFaction, false)
}
