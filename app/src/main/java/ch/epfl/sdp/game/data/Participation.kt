package ch.epfl.sdp.game.data

import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.User

data class Participation (
        var user: User,
        var ready: Boolean,
        var tag: String,
        var faction: PlayerFaction
)