package ch.epfl.sdp.replay

import ch.epfl.sdp.db.Callback

interface IReplayRepository {
    fun getAllGames(cb: Callback<List<ReplayInfo>>)
}