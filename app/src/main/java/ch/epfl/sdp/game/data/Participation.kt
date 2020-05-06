package ch.epfl.sdp.game.data

import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.user.User

data class Participation (
        var username: String,
        var ready: Boolean,
        var tag: String,
        var playerID: Int,
        var faction: PlayerFaction,
        var gameID: Int
)