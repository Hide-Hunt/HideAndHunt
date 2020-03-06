package ch.epfl.sdp.game.comm

interface RealTimePubSub {
    interface OnPublishListener {
        fun onPublish(topic: String, payload: ByteArray)
    }

    fun publish(topic: String, payload: ByteArray)
    fun subscribe(topic: String)
    fun unsubscribe(topic: String)
    fun setOnPublishListener(listener: OnPublishListener)
}