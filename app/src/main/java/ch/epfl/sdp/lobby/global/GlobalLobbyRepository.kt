package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.game.data.Game

interface GlobalLobbyRepository {
    fun getAllGames(cb: (List<Game>) -> Unit)
}