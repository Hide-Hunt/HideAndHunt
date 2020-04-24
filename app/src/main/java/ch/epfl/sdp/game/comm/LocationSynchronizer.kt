package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location

interface LocationSynchronizer {
    interface PlayerUpdateListener {
        fun onPlayerLocationUpdate(playerID: String, location: Location)
        fun onPreyCatches(predatorID: String, preyID: String)
    }

    fun updateOwnLocation(location: Location)
    fun declareCatch(playerID: String)

    fun subscribeToPlayer(playerID: String)
    fun unsubscribeFromPlayer(playerID: String)

    fun setPlayerUpdateListener(listener: PlayerUpdateListener)

    fun stop()
}