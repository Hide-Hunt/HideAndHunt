package ch.epfl.sdp.replay

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Game

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

    companion object {
        fun fromGame(userID: String, game: Game, localReplayStore: LocalReplayStore): ReplayInfo {
            val playerInfo = game.participation.first { it.userID == userID }
            return ReplayInfo(
                    game.id,
                    game.name,
                    game.creationDate.time,
                    game.endDate.time,
                    playerInfo.score,
                    playerInfo.faction,
                    localReplayStore.getFile(game.id).exists()
            )
        }
    }
}
