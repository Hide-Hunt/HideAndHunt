package ch.epfl.sdp.game.comm

/**
 * Interface that provides methods to link with a [SimpleLocationSynchronizer]
 * Provides methods required for the real time publishing / subscribing
 */
interface RealTimePubSub {
    interface OnPublishListener {
        fun onConnect()
        fun onConnectionLost()
        fun onPublish(topic: String, payload: ByteArray)
    }

    /**
     * Send a new message to the listeners
     * @param topic String: The subject of the message
     * @param payload ByteArray: The content of the message
     */
    fun publish(topic: String, payload: ByteArray)

    /**
     * Subscribe to a new topic
     * @param topic String: the topic to subscribe
     */
    fun subscribe(topic: String)

    /**
     * Unsubscribe to a topic
     * @param topic String: the topic to unsubscribe
     */
    fun unsubscribe(topic: String)

    /**
     * Define a new listener used to publish
     * @param listener OnPublishListener: the listener
     */
    fun setOnPublishListener(listener: OnPublishListener?)

    /**
     * Stop the listening
     */
    fun stop()
}