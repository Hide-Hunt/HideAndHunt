package ch.epfl.sdp.game.data

/**
 * Data class containing information about a player in a game
 */
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