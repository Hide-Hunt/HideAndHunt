package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Game

/**
 * An base interface for the global lobby interactions
 */
interface IGlobalLobbyRepository {
    /**
     * Get the list of all available games
     */
    fun getAllGames(cb: Callback<List<Game>>)
}