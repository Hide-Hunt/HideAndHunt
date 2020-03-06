package ch.epfl.sdp.game

interface LocationSynchronizer {
    fun updateOwnLocation(location: Location)
    fun subscribeToPlayer(id: Int)
    fun unsubscribeFromPlayer(id: Int)
}