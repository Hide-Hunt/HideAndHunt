package ch.epfl.sdp.replay.game_history

import ch.epfl.sdp.game.comm.GameOuterClass
import ch.epfl.sdp.game.data.*
import ch.epfl.sdp.utils.protoToGameEvent
import ch.epfl.sdp.utils.protoToPlayer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.Serializable
import java.lang.Exception

data class GameHistory(
        val gameID: Int,
        val adminID: Int,
        val players: List<Player>,
        val bounds: Area,
        val events: List<GameEvent>) : Serializable {
    open class InvalidGameHistory(msg: String) : Exception(msg)
    class InvalidFileFormat(msg: String) : InvalidGameHistory(msg)
    class InvalidGameFormat(msg: String) : InvalidGameHistory(msg)

    companion object {
        fun fromFile(inputStream: InputStream): GameHistory {
            val game = try {
                GameOuterClass.Game.parseFrom(inputStream)
            } catch (e: InvalidProtocolBufferException) {
                throw InvalidFileFormat(e.toString())
            }

            return fromProtobufObject(game)
        }

        private fun fromProtobufObject(game: GameOuterClass.Game): GameHistory {
            if (game.id < 0) throw InvalidGameFormat("Invalid game id")
            if (game.adminID < 0) throw InvalidGameFormat("Invalid admin id")

            val players = game.playersList.map { it.protoToPlayer() }
            if (players.isEmpty()) throw InvalidGameFormat("No player")

            val events = game.eventsList.map { it.protoToGameEvent() }
            if (events.isEmpty()) throw InvalidGameFormat("No event")

            players.forEach { player ->
                try {
                    val firstLoc = events.first { it is LocationEvent && it.playerID == player.id }
                    player.lastKnownLocation = (firstLoc as LocationEvent).location
                } catch (_: NoSuchElementException) { /* The player has no associated location event */
                }
            }

            val firstLocation = (events.first { it is LocationEvent } as LocationEvent).location
            val gameArea = events.fold(Area(firstLocation, firstLocation), { tmpArea, event ->
                if (event is LocationEvent) tmpArea.increase(event.location) else tmpArea
            })

            return GameHistory(game.id, game.adminID, players, gameArea, events)
        }
    }
}