package ch.epfl.sdp.game.comm

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import ch.epfl.sdp.game.Location
import ch.epfl.sdp.game.PlayerUpdateListener
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MQTTRealTimePubSub internal constructor(context: Context, private val gameID: Int) : PlayerUpdateListener {
    private val mqttAndroidClient: MqttAndroidClient
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

    @SuppressLint("DefaultLocale")
    override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
        try {
            val payload = LocationOuterClass.Location.newBuilder()
                    .setLatitude(location.latitude)
                    .setLongitude(location.longitude)
                    .build()
            val message = MqttMessage(payload.toByteArray())
            mqttAndroidClient.publish(String.format("%d/%d", gameID, playerID), message)
        } catch (e: MqttException) {
            System.err.println("Error Publishing: " + e.message)
            e.printStackTrace()
        }
    }

    override fun onPreyCatches(playerID: Int) { // TODO Send the message on MQTT
    }

    companion object {
        private val CLIENT_ID = "Hide&Hunt" + System.currentTimeMillis()
        private const val SERVER_URI = "tcp://kangaroo.rmq.cloudamqp.com:1883"
        private const val USERNAME = "jprnwwdd:jprnwwdd"
        private const val PASSWORD = "aDs-KxLr1cTqGgBSWKExxmCUXBcsWfOE"
    }

    init {
        mqttAndroidClient = MqttAndroidClient(context, SERVER_URI, CLIENT_ID)
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.w("mqtt", s)
            }

            override fun connectionLost(throwable: Throwable) {}
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Mqtt", mqttMessage.toString())
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {}
        })
        connect()
    }
}