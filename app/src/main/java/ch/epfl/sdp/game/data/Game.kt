package ch.epfl.sdp.game.data

import java.util.*

/**
 * Data class containing information about a create game
 */
data class Game(
        var id: String,
        var name: String,
        var adminID: String,
        var duration: Long,
        val params: Map<String, GameOption>,
        var participation: List<Participation>,
        var creationDate: Date,
        var startDate: Date,
        var endDate: Date,
        var state: GameState
) {
    // Empty constructor for injection
    constructor(): this("","", "", 0,
            emptyMap(), emptyList(),
            Date(), Date(), Date(), GameState.LOBBY)
}