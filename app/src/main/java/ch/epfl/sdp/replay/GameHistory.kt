package ch.epfl.sdp.replay

import ch.epfl.sdp.game.data.Area
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.replay.game_event.GameEvent
import java.io.Serializable

data class GameHistory(
        val players: List<Player>,
        val bounds: Area,
        val events: List<GameEvent>) : Serializable