package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.DB
import ch.epfl.sdp.game.data.Game

class GlobalLobbyRepository(private val database: DB) : IGlobalLobbyRepository {

    override fun getAllGames(cb: Callback<List<Game>>) {
        database.getAllGames(cb)
    }
}