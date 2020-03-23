package ch.epfl.sdp.replay.game_event

import java.io.Serializable
import java.lang.Exception

abstract class GameEvent(val timestamp: Int) : Serializable {
    class WrongGameEventFormat : Exception()
}
