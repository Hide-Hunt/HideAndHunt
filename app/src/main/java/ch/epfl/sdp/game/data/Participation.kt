package ch.epfl.sdp.game.data

import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.User

class Participation (
        var user: User,
        var ready: Boolean,
        var tag: String,
        var faction: PlayerParametersFragment.Faction
)