package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.Location

class LocationEmitter(private val gameID: Int, private val ownPlayerID: Int, private val pubSub: RealTimePubSub) : LocationSynchronizer {
    override fun updateOwnLocation(location: Location) {
        val payload = LocationOuterClass.Location.newBuilder()
                .setLatitude(location.latitude)
                .setLongitude(location.longitude)
                .build()
        pubSub.publish("$gameID/$ownPlayerID", payload.toByteArray())
    }

    override fun subscribeToPlayer(playerID: Int) {
        pubSub.subscribe("$gameID/$playerID")
    }

    override fun unsubscribeFromPlayer(playerID: Int) {
        pubSub.unsubscribe("$gameID/$playerID")
    }
}