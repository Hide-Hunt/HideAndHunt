package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Game

/**
 * A base interface for the global lobby interactions
 */
interface IGlobalLobbyRepository {
    /**
     * Get the list of all available games
     * @param cb Callback<List<Game>>: a callback to operate over the games's list
     */
    fun getAllGames(cb: Callback<List<Game>>)
}