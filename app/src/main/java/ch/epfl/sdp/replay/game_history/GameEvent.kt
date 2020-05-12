package ch.epfl.sdp.replay.game_history

import java.io.Serializable

abstract class GameEvent(val timestamp: Int) : Serializable {
    class WrongGameEventFormat : Exception()
}
