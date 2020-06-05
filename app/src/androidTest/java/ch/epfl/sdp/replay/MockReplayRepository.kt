package ch.epfl.sdp.replay

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Faction

class MockReplayRepository : IReplayRepository {
    private val mockReplayList = listOf(
            ReplayInfo("0", "Game #0", 0, 2345, "", Faction.PREDATOR, true),
            ReplayInfo("1", "Game #1", 6753759194, 6753759194 + 675, "", Faction.PREDATOR, false),
            ReplayInfo("2", "Game #2", 964781131, 964781131 + 182, "", Faction.PREY, false),
            ReplayInfo("3", "Game #3", 1982211276, 1982211276 + 871, "", Faction.PREDATOR, false),
            ReplayInfo("4", "Game #4", 5893518155, 5893518155 + 139, "", Faction.PREY, false),
            ReplayInfo("5", "Game #5", 8505536244, 8505536244 + 549, "", Faction.PREY, false)
    )

    override fun getAllGames(userID: String, cb: Callback<List<ReplayInfo>>) {
        cb(mockReplayList)
    }
}