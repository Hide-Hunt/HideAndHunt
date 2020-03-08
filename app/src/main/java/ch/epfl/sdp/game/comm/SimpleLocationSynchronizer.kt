package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location

class SimpleLocationSynchronizer(private val gameID: Int, private val ownPlayerID: Int, private val pubSub: RealTimePubSub) : LocationSynchronizer {
    private var listener : LocationSynchronizer.PlayerUpdateListener? = null
    private val topicOffset = gameID.toString().length // gameID + char('/')

    init {
        pubSub.setOnPublishListener(object : RealTimePubSub.OnPublishListener {
            override fun onPublish(topic: String, payload: ByteArray) {
                val channel = topic.substring(topicOffset)
                if (channel == "catch") {
                    listener?.onPreyCatches(CatchOuterClass.Catch.parseFrom(payload).preyID)
                } else {
                    try {
                        val playerID = channel.toInt()
                        val protoLoc = LocationOuterClass.Location.parseFrom(payload)
                        listener?.onPlayerLocationUpdate(playerID, Location(protoLoc.latitude, protoLoc.longitude))
                    } catch (_: NumberFormatException) {}
                }
            }
        })
    }

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

    override fun setPlayerUpdateListener(listener: LocationSynchronizer.PlayerUpdateListener) {
        this.listener = listener
    }
}