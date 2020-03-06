package ch.epfl.sdp.game

class Player(var id: Int, var faction: Faction) {
    var lastKnownLocation: Location? = null

    override fun toString(): String {
        return "Player{" +
                "id=" + id +
                ", faction=" + faction +
                ", lastKnownLocation=" + lastKnownLocation +
                '}'
    }

}