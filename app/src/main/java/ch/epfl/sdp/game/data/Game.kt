package ch.epfl.sdp.game.data

import java.io.Serializable
import java.util.*

data class Game(
        var id: Int,
        var name: String,
        var admin: String,
        var duration: Int,
        val params: Map<String, GameOption>,
        var state: GameState,
        var participation: List<Participation>,
        var startDate: Date,
        var endDate: Date,
        var creationDate: Date
): Serializable