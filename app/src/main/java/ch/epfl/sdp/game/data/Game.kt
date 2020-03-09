package ch.epfl.sdp.game.data

import java.util.*
import kotlin.collections.HashMap

class Game (
        var id: Int = 0,
        var name: String = "",
        var admin: String = "",
        var duration: Int = 0,
        val params: Map<String, GameOption> = HashMap(),
        var state: GameState = GameState.LOBBY,
        var participation: Participation = Participation(),
        var startDate: Date = Date(),
        var endDate: Date = Date(),
        var creationDate: Date = Date()
)