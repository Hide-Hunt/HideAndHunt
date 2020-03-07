package ch.epfl.sdp.game

import ch.epfl.sdp.game.data.Location

interface LocationSynchronizer {
    fun updateOwnLocation(location: Location)
    fun subscribeToPlayer(id: Int)
    fun unsubscribeFromPlayer(id: Int)
}