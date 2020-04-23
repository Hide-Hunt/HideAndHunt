package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location

class SimpleLocationSynchronizer(private val gameID: Int, private val ownPlayerId: String, private val pubSub: RealTimePubSub) : LocationSynchronizer {
    var listener : LocationSynchronizer.PlayerUpdateListener? = null
    private val topicOffset = gameID.toString().length + 1// gameID + char('/')

    init {
        pubSub.setOnPublishListener(object : RealTimePubSub.OnPublishListener {
            override fun onConnect() {
                pubSub.subscribe("$gameID/catch")
            }

            override fun onConnectionLost() {}

            override fun onPublish(topic: String, payload: ByteArray) {
                val channel = topic.substring(topicOffset)
                if (channel == "catch") {
                    val catch = CatchOuterClass.Catch.parseFrom(payload)
                    listener?.onPreyCatches(catch.predatorID, catch.preyID)
                } else {
                    try {
                        val protoLoc = LocationOuterClass.Location.parseFrom(payload)
                        listener?.onPlayerLocationUpdate(channel, Location(protoLoc.latitude, protoLoc.longitude))
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
        pubSub.publish("$gameID/$ownPlayerId", payload.toByteArray())
    }

    override fun declareCatch(playerId: String) {
        val payload = CatchOuterClass.Catch.newBuilder()
                .setPredatorID(ownPlayerId)
                .setPreyID(playerId)
                .build()
        pubSub.publish("$gameID/catch", payload.toByteArray())
    }

    override fun subscribeToPlayer(playerId: String) {
        pubSub.subscribe("$gameID/$playerId")
    }

    override fun unsubscribeFromPlayer(playerId: String) {
        pubSub.unsubscribe("$gameID/$playerId")
    }

    override fun setPlayerUpdateListener(listener: LocationSynchronizer.PlayerUpdateListener) {
        this.listener = listener
    }

    override fun stop() {
        pubSub.stop()
        pubSub.setOnPublishListener(null)
        listener = null
    }
}