package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location

class SimpleLocationSynchronizer(private val gameID: Int, private val ownPlayerID: Int, private val pubSub: RealTimePubSub) : LocationSynchronizer {
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
                    listener?.onPreyCatches(catch.predatorID.toInt(), catch.preyID.toInt())
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

    override fun declareCatch(playerID: Int) {
        val payload = CatchOuterClass.Catch.newBuilder()
                .setPredatorID(ownPlayerID.toString())
                .setPreyID(playerID.toString())
                .build()
        pubSub.publish("$gameID/catch", payload.toByteArray())
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

    override fun stop() {
        pubSub.stop()
        pubSub.setOnPublishListener(null)
        listener = null
    }
}