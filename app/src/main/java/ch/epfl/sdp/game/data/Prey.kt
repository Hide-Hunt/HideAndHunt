package ch.epfl.sdp.game.data

/**
 * Class describing a Prey, inherit from [Player]
 * @param id Int: the prey's ID
 * @param NFCTag String: the prey's NFC Tag, as a String
 */
class Prey(id: Int, var NFCTag: String = "") : Player(id) {
    var state = PreyState.ALIVE
}