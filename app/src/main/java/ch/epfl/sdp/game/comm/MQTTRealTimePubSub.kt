package ch.epfl.sdp.game.comm

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MQTTRealTimePubSub internal constructor(context: Context) : RealTimePubSub {
    private var listener: RealTimePubSub.OnPublishListener? = null
    private val mqttAndroidClient: MqttAndroidClient

    init {
        mqttAndroidClient = MqttAndroidClient(context, SERVER_URI, CLIENT_ID)
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.w("mqtt", s)
            }

            override fun connectionLost(throwable: Throwable) {}
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Mqtt", mqttMessage.toString())
                listener?.onPublish(topic, mqttMessage.payload)
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {}
        })
        connect()
    }

    private fun connect() {
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false
        mqttConnectOptions.userName = USERNAME
        mqttConnectOptions.password = PASSWORD.toCharArray()

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    val disconnectedBufferOptions = DisconnectedBufferOptions()
                    disconnectedBufferOptions.isBufferEnabled = true
                    disconnectedBufferOptions.bufferSize = 100
                    disconnectedBufferOptions.isPersistBuffer = false
                    disconnectedBufferOptions.isDeleteOldestMessages = false
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.w("Mqtt", "Failed to connect to: $SERVER_URI$exception")
                }
            })
        } catch (ex: MqttException) {
            ex.printStackTrace()
        }
    }

    override fun publish(topic: String, payload: ByteArray) {
        try {
            val message = MqttMessage(payload)
            mqttAndroidClient.publish(topic, message)
        } catch (e: MqttException) {
            System.err.println("Error Publishing: " + e.message)
            e.printStackTrace()
        }
    }

    override fun subscribe(topic: String) {
        mqttAndroidClient.subscribe(topic, 0)
    }

    override fun unsubscribe(topic: String) {
        mqttAndroidClient.unsubscribe(topic)
    }

    override fun setOnPublishListener(listener: RealTimePubSub.OnPublishListener) {
        this.listener = listener
    }

    companion object {
        private val CLIENT_ID = "Hide&Hunt" + System.currentTimeMillis()
        private const val SERVER_URI = "tcp://kangaroo.rmq.cloudamqp.com:1883"
        private const val USERNAME = "jprnwwdd:jprnwwdd"
        private const val PASSWORD = "aDs-KxLr1cTqGgBSWKExxmCUXBcsWfOE"
    }
}