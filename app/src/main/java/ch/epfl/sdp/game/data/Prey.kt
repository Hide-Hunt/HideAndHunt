package ch.epfl.sdp.game.data

class Prey(id: String, var NFCTag: String = "") : Player(id) {
    var state = PreyState.ALIVE
}