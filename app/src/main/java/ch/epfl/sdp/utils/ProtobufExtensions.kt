package ch.epfl.sdp.utils

import ch.epfl.sdp.game.comm.GameEventOuterClass
import ch.epfl.sdp.game.comm.GameOuterClass
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import ch.epfl.sdp.replay.game_history.CatchEvent
import ch.epfl.sdp.replay.game_history.GameEvent
import ch.epfl.sdp.replay.game_history.LocationEvent


fun GameEventOuterClass.GameEvent.protoToGameEvent(): GameEvent {
    return when (payloadCase) {
        GameEventOuterClass.GameEvent.PayloadCase.CATCH_EVENT -> catchEvent.let {
            CatchEvent(timestamp, it.predatorID, it.preyID)
        }

        GameEventOuterClass.GameEvent.PayloadCase.LOCATION_EVENT -> locationEvent.let {
            LocationEvent(timestamp, it.playerID, Location(it.location.latitude, it.location.longitude))
        }

        else -> throw GameEvent.WrongGameEventFormat()
    }
}

fun GameOuterClass.Player.protoToPlayer(): Player {
    return when (faction) {
        GameOuterClass.Faction.PREY -> Prey(id)
        GameOuterClass.Faction.PREDATOR -> Predator(id)
        else -> throw Player.WrongPlayerFormat()
    }
}