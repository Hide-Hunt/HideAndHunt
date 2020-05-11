package ch.epfl.sdp.game.data

data class Participation(
        var userID: String,
        var faction: Faction,
        var ready: Boolean,
        var tag: String,
        var score: String
) {
    constructor(): this("", Faction.PREDATOR, false, "", "")
    fun toPlayer(playerID: Int) : Player =
            if (faction == Faction.PREY) Prey(playerID, tag) else Predator(playerID)
}