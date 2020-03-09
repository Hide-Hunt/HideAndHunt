package ch.epfl.sdp.game.data

import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.User

class Participation (
        var user: User = User(),
        var ready: Boolean = false,
        var tag: String = "",
        var faction: PlayerParametersFragment.Faction = PlayerParametersFragment.Faction.PREY
)