package ch.epfl.sdp.game.data

class Prey(id: Int, var NFCTag: String = "") : Player(id) {
    enum class PreyState{
        ALIVE, DEAD
    }
    var state = PreyState.ALIVE
}