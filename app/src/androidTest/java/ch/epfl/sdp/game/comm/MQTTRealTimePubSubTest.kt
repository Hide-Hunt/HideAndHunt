package ch.epfl.sdp.game.comm

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import org.junit.Test

class MQTTRealTimePubSubTest {
    class EmptyFragment : Fragment()

    @Test
    fun shouldNotCrashIfNotConnected() {
        val scenario = launchFragment<EmptyFragment>()
        scenario.onFragment {
            val mqttRealTimePubSub = MQTTRealTimePubSub(it.context!!, null)
            mqttRealTimePubSub.publish("a", ByteArray(0))
            mqttRealTimePubSub.subscribe("a")
            mqttRealTimePubSub.unsubscribe("a")
            mqttRealTimePubSub.setOnPublishListener(object : RealTimePubSub.OnPublishListener {
                override fun onConnect() {}

                override fun onConnectionLost() {}

                override fun onPublish(topic: String, payload: ByteArray) {}
            })
        }
    }

    @Test
    fun shouldNotCrashIfUnsubscribeWithoutSubscription() {
        val scenario = launchFragment<EmptyFragment>()
        scenario.onFragment {
            MQTTRealTimePubSub(it.context!!, null).unsubscribe("a")
        }
    }
}