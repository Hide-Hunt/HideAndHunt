package ch.epfl.sdp.replay.game_event

import java.io.Serializable

abstract class GameEvent(val timestamp: Int) : Serializable
