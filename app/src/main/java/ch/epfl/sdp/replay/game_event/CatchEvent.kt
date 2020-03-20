package ch.epfl.sdp.replay.game_event

class CatchEvent(timestamp: Int, val predatorID: Int, val preyID: Int) : GameEvent(timestamp)