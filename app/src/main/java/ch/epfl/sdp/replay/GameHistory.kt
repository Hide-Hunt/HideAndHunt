package ch.epfl.sdp.replay

import ch.epfl.sdp.game.comm.GameOuterClass
import ch.epfl.sdp.game.data.*
import ch.epfl.sdp.replay.game_event.GameEvent
import ch.epfl.sdp.replay.game_event.LocationEvent
import ch.epfl.sdp.utils.protoToGameEvent
import ch.epfl.sdp.utils.protoToPlayer
import java.io.InputStream
import java.io.Serializable
import java.lang.Exception
import java.util.*

data class GameHistory(
        val gameID: Int,
        val adminID: Int,
        val players: List<Player>,
        val bounds: Area,
        val events: List<GameEvent>) : Serializable {
    class WrongGameFormat : Exception()

    companion object {
        fun fromFile(inputStream: InputStream): GameHistory {
            val game = GameOuterClass.Game.parseFrom(inputStream)

            val events = game.eventsList.map { it.protoToGameEvent() }

            val players = game.playersList.map { it.protoToPlayer() }
            players.forEach {player ->
                val firstLoc = events.first { it is LocationEvent && it.playerID == player.id }
                player.lastKnownLocation = (firstLoc as LocationEvent).location
            }

            val firstLocation = (events.first { it is LocationEvent } as LocationEvent).location
            val gameArea = events.fold(Area(firstLocation, firstLocation), { tmpArea, event ->
                if (event is LocationEvent) tmpArea.increase(event.location) else tmpArea
            })

            return GameHistory(game.id, game.adminID, players, gameArea, events)
        }
    }
}