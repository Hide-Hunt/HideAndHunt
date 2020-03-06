package ch.epfl.sdp.game

interface PlayerUpdateListener {
    fun onPlayerLocationUpdate(playerID: Int, location: Location)
    fun onPreyCatches(playerID: Int)
}