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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (id != other.id) return false
        if (lastKnownLocation != other.lastKnownLocation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (lastKnownLocation?.hashCode() ?: 0)
        return result
    }
}