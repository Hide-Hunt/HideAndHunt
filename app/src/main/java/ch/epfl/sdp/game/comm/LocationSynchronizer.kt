package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location

interface LocationSynchronizer {
    interface PlayerUpdateListener {
        fun onPlayerLocationUpdate(playerId: Int, location: Location)
        fun onPreyCatches(predatorId: Int, preyId: Int)
    }

    fun updateOwnLocation(location: Location)
    fun declareCatch(playerId: Int)

    fun subscribeToPlayer(playerId: Int)
    fun unsubscribeFromPlayer(playerId: Int)

    fun setPlayerUpdateListener(listener: PlayerUpdateListener)

    fun stop()
}