package ch.epfl.sdp.replay.game_event

import ch.epfl.sdp.game.data.Location

class LocationEvent(timestamp: Long, val playerID: Int, val location: Location) : GameEvent(timestamp)