package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.Location
import org.junit.Test

import org.junit.Assert.*

class LocationEmitterTest {
    class FakePubSub : RealTimePubSub {
        var expectedTopic: String = ""
        var expectedPayload: ByteArray = ByteArray(0)
        var listener: RealTimePubSub.OnPublishListener? = null

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

        override fun setOnPublishListener(listener: RealTimePubSub.OnPublishListener) {
            this.listener = listener
        }

    }

    @Test
    fun updateOwnLocation() {
        val location = Location(24.0, 42.0)
        val pubSub = FakePubSub()
        val payload = LocationOuterClass.Location.newBuilder()
                .setLatitude(location.latitude)
                .setLongitude(location.longitude)
                .build()

        val locationEmitter = LocationEmitter(12, 34, pubSub)
        pubSub.expectedTopic = "12/34"
        pubSub.expectedPayload = payload.toByteArray()
        locationEmitter.updateOwnLocation(location)
    }

    @Test
    fun subscribeToPlayer() {
        val pubSub = FakePubSub()
        val locationEmitter = LocationEmitter(12, 34, pubSub)
        pubSub.expectedTopic = "12/34"
        locationEmitter.subscribeToPlayer(34)
    }

    @Test
    fun unsubscribeFromPlayer() {
        val pubSub = FakePubSub()
        val locationEmitter = LocationEmitter(12, 34, pubSub)
        pubSub.expectedTopic = "12/34"
        locationEmitter.unsubscribeFromPlayer(34)
    }
}