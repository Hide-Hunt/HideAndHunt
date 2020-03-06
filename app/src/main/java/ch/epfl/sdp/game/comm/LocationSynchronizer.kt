package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.Location

interface LocationSynchronizer {
    fun updateOwnLocation(location: Location)
    fun subscribeToPlayer(playerID: Int)
    fun unsubscribeFromPlayer(playerID: Int)
}