package ch.epfl.sdp.game.data

import java.io.Serializable

/**
 * Class describing a Player
 * @param id Int: the player's ID
 */
open class Player(val id: Int) : Serializable {
    class WrongPlayerFormat: Exception()

    var lastKnownLocation: Location? = null

    override fun toString(): String {
        return "Player{" +
                "id=" + id +
                ", lastKnownLocation=" + lastKnownLocation +
                '}'
    }
}