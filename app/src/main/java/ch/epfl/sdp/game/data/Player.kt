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

    override fun equals(other: Any?): Boolean =
            this === other || (other is Player && other.id == id && other.lastKnownLocation == lastKnownLocation)

    override fun hashCode(): Int = 31 * id + (lastKnownLocation?.hashCode() ?: 0)
}