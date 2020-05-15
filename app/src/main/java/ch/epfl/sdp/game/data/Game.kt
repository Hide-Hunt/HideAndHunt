package ch.epfl.sdp.game.data

import java.util.*

/**
 * Data class containing information about a create game
 */
data class Game(
        var id: Int,
        var name: String,
        var admin: String,
        var duration: Long,
        val params: Map<String, GameOption>,
        var state: GameState,
        var participation: List<Participation>,
        var startDate: Date,
        var endDate: Date,
        var creationDate: Date,
        var adminID: Int
)