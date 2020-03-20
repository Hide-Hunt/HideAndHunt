package ch.epfl.sdp.replay.game_event

class CatchEvent(timestamp: Long, val predatorID: Int, val preyID: Int) : GameEvent(timestamp)