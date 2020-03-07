package ch.epfl.sdp.game

import ch.epfl.sdp.game.data.Location

interface PlayerUpdateListener {
    fun OnPlayerLocationUpdate(id: Int, location: Location)
    fun OnPreyCatches(id: Int)
}