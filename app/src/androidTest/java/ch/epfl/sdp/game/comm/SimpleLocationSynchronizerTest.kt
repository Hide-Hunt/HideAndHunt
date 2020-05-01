package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location
import org.junit.Test

import org.junit.Assert.*

class SimpleLocationSynchronizerTest {
    class FakePubSub(private val gameID: Int) : RealTimePubSub {
        var expectedTopic: String = ""
        var expectedPayload: ByteArray = ByteArray(0)
        private var listener: RealTimePubSub.OnPublishListener? = null

        override fun publish(topic: String, payload: ByteArray) {
            assertEquals(expectedTopic, topic)
            assertEquals(expectedPayload.asList(), payload.asList())
        }

        override fun subscribe(topic: String) {
            assertEquals(expectedTopic, topic)
        }

        override fun unsubscribe(topic: String) {
            assertEquals(expectedTopic, topic)
        }

        override fun setOnPublishListener(listener: RealTimePubSub.OnPublishListener?) {
            this.listener = listener
        }

        override fun stop() {}

        fun sendLocationUpdate(playerID: Int, location: Location) {
            val protoLoc = LocationOuterClass.Location.newBuilder()
                    .setLatitude(location.latitude)
                    .setLongitude(location.longitude)
                    .build()
            listener?.onPublish("${gameID}/${playerID}", protoLoc.toByteArray())
        }

        fun sendCatchPublish(predatorID: Int, preyID: Int) {
            val catch = CatchOuterClass.Catch.newBuilder()
                    .setPredatorID(predatorID.toString())
                    .setPreyID(preyID.toString())
                    .build()
            listener?.onPublish("${gameID}/catch", catch.toByteArray())
        }
    }

    @Test
    fun updateOwnLocation() {
        val location = Location(24.0, 42.0)
        val pubSub = FakePubSub(12)
        val payload = LocationOuterClass.Location.newBuilder()
                .setLatitude(location.latitude)
                .setLongitude(location.longitude)
                .build()

        val locationEmitter = SimpleLocationSynchronizer(12, 34, pubSub)
        pubSub.expectedTopic = "12/34"
        pubSub.expectedPayload = payload.toByteArray()
        locationEmitter.updateOwnLocation(location)
    }

    @Test
    fun subscribeToPlayer() {
        val pubSub = FakePubSub(12)
        val locationEmitter = SimpleLocationSynchronizer(12, 34, pubSub)
        pubSub.expectedTopic = "12/34"
        locationEmitter.subscribeToPlayer(34)
    }

    @Test
    fun unsubscribeFromPlayer() {
        val pubSub = FakePubSub(12)
        val locationEmitter = SimpleLocationSynchronizer(12, 34, pubSub)
        pubSub.expectedTopic = "12/34"
        locationEmitter.unsubscribeFromPlayer(34)
    }

    @Test
    fun onPlayerLocationUpdateShouldBeCalledOnPlayerLocationUpdate() {
        val pubSub = FakePubSub(12)
        val locationEmitter = SimpleLocationSynchronizer(12, 34, pubSub)

        var gotExpectedMessage = false
        val expectedLocation = Location(42.0, 24.0)

        locationEmitter.setPlayerUpdateListener(object : LocationSynchronizer.PlayerUpdateListener {
            override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
                assertEquals(42, playerID)
                assertEquals(expectedLocation.latitude, location.latitude, 0.001)
                assertEquals(expectedLocation.longitude, location.longitude, 0.001)
                gotExpectedMessage = true
            }

            override fun onPreyCatches(predatorID: Int, preyID: Int) {
                fail()
            }
        })

        pubSub.sendLocationUpdate(42, expectedLocation)

        assertTrue(gotExpectedMessage)
    }

    @Test
    fun onPreyCatchesShouldBeCalledOnCatchPublish() {
        val pubSub = FakePubSub(12)
        val locationEmitter = SimpleLocationSynchronizer(12, 34, pubSub)

        var gotExpectedMessage = false

        locationEmitter.setPlayerUpdateListener(object : LocationSynchronizer.PlayerUpdateListener {
            override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
                fail()
            }

            override fun onPreyCatches(predatorID: Int, preyID: Int) {
                assertEquals(42, predatorID)
                assertEquals(24, preyID)
                gotExpectedMessage = true
            }
        })

        pubSub.sendCatchPublish(42, 24)

        assertTrue(gotExpectedMessage)
    }
}