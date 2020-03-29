package ch.epfl.sdp.replay

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Game
import java.io.File

interface IReplayRepository {
    fun getAllGames(cb: Callback<List<ReplayInfo>>)
}