package ch.epfl.sdp.replay

import ch.epfl.sdp.game.data.Faction

data class ReplayInfo(val gameID: Int, val startTimestamp: Int, val endTimestamp: Int, val winningFaction: Faction)
