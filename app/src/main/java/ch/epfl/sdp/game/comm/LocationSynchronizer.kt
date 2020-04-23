package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location

interface LocationSynchronizer {
    interface PlayerUpdateListener {
        fun onPlayerLocationUpdate(playerId: String, location: Location)
        fun onPreyCatches(predatorId: String, preyId: String)
    }

    fun updateOwnLocation(location: Location)
    fun declareCatch(playerId: String)

    fun subscribeToPlayer(playerId: String)
    fun unsubscribeFromPlayer(playerId: String)

    fun setPlayerUpdateListener(listener: PlayerUpdateListener)

    fun stop()
}