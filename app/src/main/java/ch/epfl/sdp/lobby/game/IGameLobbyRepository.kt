package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
import java.sql.Time

/**
 * An base interface for the game lobby interactions
 */
interface IGameLobbyRepository {

    /**
     * Register the current user in the game associated to the given id
     * @param gameId id of the game to join
     */
    fun addLocalParticipation(gameId: Int)

    /**
     * Create name with specified name and duration
     * @param gameName name of the game to be created
     * @param gameDuration duration in minutes
     */
    fun createGame(gameName: String, gameDuration: Long): Int

    /**
     * Get a game's name given its id
     */
    fun getGameName(gameId: Int, cb: Callback<String>)

    /**
     * Get a game's duration given its id
     */
    fun getGameDuration(gameId: Int, cb: Callback<Long>)

    /**
     * Get a game's list of players given its id
     */
    fun getPlayers(gameId: Int, cb: Callback<List<Player>>)

    /**
     * Get a game's list of participations given its id
     */
    fun getParticipations(gameId: Int, cb: Callback<List<Participation>>)

    /**
     * Get a game's admin's id given the game id
     */
    fun getAdminId(gameId: Int, cb: Callback<Int>)

    /**
     * Change the ready attribute of a player
     * @param gameId the id of the game the player is in
     * @param uid the id of the player
     */
    fun changePlayerReady(gameId: Int, uid: Int)

    /**
     * Sets the ready attribute to a given value
     * @param gameId the id of the game the player is in
     * @param uid the id of the player
     */
    fun setPlayerReady(gameId: Int, uid: Int, ready: Boolean)

    /**
     * Sets the faction of the player to the given value
     * @param gameId the id of the game the player is in
     * @param uid the id of the player
     */
    fun setPlayerFaction(gameId: Int, uid: Int, faction: PlayerFaction)

    /**
     * Sets the nfc tag of the player to the given value
     * @param gameId the id of the game the player is in
     * @param uid the id of the player
     */
    fun setPlayerTag(gameId: Int, uid: Int, tag: String)

    /**
     * Remove a participation from a game lobby
     * @param gameId the id of the game
     */
    fun removeLocalParticipation(gameId: Int)
}