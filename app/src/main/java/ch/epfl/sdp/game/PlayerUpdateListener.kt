package ch.epfl.sdp.game

interface PlayerUpdateListener {
    fun OnPlayerLocationUpdate(id: Int, location: Location)
    fun OnPreyCatches(id: Int)
}