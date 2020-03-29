package ch.epfl.sdp.replay

import ch.epfl.sdp.game.data.Faction

data class ReplayInfo(
        val gameID: Int,
        val startTimestamp: Long,
        val endTimestamp: Long,
        val winningFaction: Faction,
        var localCopy: Boolean
)
