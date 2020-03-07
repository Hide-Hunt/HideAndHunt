package ch.epfl.sdp.game.data

import java.io.Serializable

open class Player(var id: Int, var faction: Faction, var NFCTag: String = "") : Serializable {
    var lastKnownLocation: Location? = null

    override fun toString(): String {
        return "Player{" +
                "id=" + id +
                ", faction=" + faction +
                ", lastKnownLocation=" + lastKnownLocation +
                ", NFCTag=" + NFCTag +
                '}'
    }

}