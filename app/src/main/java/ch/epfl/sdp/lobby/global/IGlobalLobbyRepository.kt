package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Game

interface IGlobalLobbyRepository {
    fun getAllGames(cb: Callback<List<Game>>)
}