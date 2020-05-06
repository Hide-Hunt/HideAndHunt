package ch.epfl.sdp.game.data

import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.user.User

data class Participation (
        var ready: Boolean,
        var tag: String,
        var playerID: Int,
        var faction: PlayerFaction
)