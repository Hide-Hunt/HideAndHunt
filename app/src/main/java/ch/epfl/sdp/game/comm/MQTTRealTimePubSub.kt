package ch.epfl.sdp.game.comm

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

/**
 * MQTT implementation of [RealTimePubSub]
 * @param context Context: The [Context] of the current Activity
 * @param uri String: The MQTT server uri
 */
class MQTTRealTimePubSub internal constructor(context: Context, uri: String?) : RealTimePubSub {
    private var listener: RealTimePubSub.OnPublishListener? = null
    private val mqttAndroidClient: MqttAndroidClient
    private val pendingSub = ArrayList<String>()

    init {
        mqttAndroidClient = MqttAndroidClient(context, uri ?: SERVER_URI, CLIENT_ID)
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.w("mqtt", "connected to: $s")
                listener?.onConnect()
            }

            override fun connectionLost(throwable: Throwable) {
                Log.w("mqtt", "connection lost")
                listener?.onConnectionLost()
            }

            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("mqtt", mqttMessage.toString())
                listener?.onPublish(topic, mqttMessage.payload)
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {
                Log.w("mqtt", "delivery complete")
            }
        })
        connect()
    }

    private fun connect() {
        try {
            val disconnectedBufferOptions = DisconnectedBufferOptions()
            disconnectedBufferOptions.isBufferEnabled = true
            disconnectedBufferOptions.bufferSize = 100
            disconnectedBufferOptions.isPersistBuffer = false
            disconnectedBufferOptions.isDeleteOldestMessages = false

            val mqttConnectOptions = MqttConnectOptions()
            mqttConnectOptions.isAutomaticReconnect = true
            mqttConnectOptions.isCleanSession = false
            mqttConnectOptions.userName = USERNAME
            mqttConnectOptions.password = PASSWORD.toCharArray()
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions)
                    pendingSub.forEach { subscribe(it) }
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.w("Mqtt", "Failed to connect to: $SERVER_URI$exception")
                }
            })
        } catch (ex: MqttException) { ex.printStackTrace() }
    }

    override fun stop() {
        mqttAndroidClient.close()
    }

    override fun publish(topic: String, payload: ByteArray) {
        if (!mqttAndroidClient.isConnected) return

        try {
            val message = MqttMessage(payload)
            mqttAndroidClient.publish(topic, message)
        } catch (e: MqttException) {
            System.err.println("Error Publishing: " + e.message)
            e.printStackTrace()
        }
    }

    override fun subscribe(topic: String) {
        if (!mqttAndroidClient.isConnected) pendingSub.add(topic)
        else mqttAndroidClient.subscribe(topic, 0)
    }

    override fun unsubscribe(topic: String) {
        if (!mqttAndroidClient.isConnected) return
        mqttAndroidClient.unsubscribe(topic)
    }

    override fun setOnPublishListener(listener: RealTimePubSub.OnPublishListener?) {
        this.listener = listener
    }

    //TODO : Hide password in a gitignore file
    companion object {
        private val CLIENT_ID = "Hide&Hunt" + System.currentTimeMillis()
        private const val SERVER_URI = "tcp://kangaroo.rmq.cloudamqp.com:1883"
        private const val USERNAME = "jprnwwdd:jprnwwdd"
        private const val PASSWORD = "aDs-KxLr1cTqGgBSWKExxmCUXBcsWfOE"
    }
}