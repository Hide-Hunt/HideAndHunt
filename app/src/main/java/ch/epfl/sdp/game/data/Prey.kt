package ch.epfl.sdp.game.data

class Prey(id: Int, var NFCTag: String = "") : Player(id) {
    var state = PreyState.ALIVE
}