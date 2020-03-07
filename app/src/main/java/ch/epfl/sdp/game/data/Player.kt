package ch.epfl.sdp.game.data

import java.io.Serializable

open class Player(var id: Int) : Serializable {
    var lastKnownLocation: Location? = null

    override fun toString(): String {
        return "Player{" +
                "id=" + id +
                ", lastKnownLocation=" + lastKnownLocation +
                '}'
    }

}