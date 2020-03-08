package ch.epfl.sdp.game

import ch.epfl.sdp.game.data.Location

interface PlayerUpdateListener {
    fun onPlayerLocationUpdate(playerID: Int, location: Location)
    fun onPreyCatches(playerID: Int)
}