package ch.epfl.sdp.replay.game_history

import ch.epfl.sdp.game.data.Location

class LocationEvent(timestamp: Int, val playerID: Int, val location: Location) : GameEvent(timestamp)