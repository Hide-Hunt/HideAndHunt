package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player

/**
 * An base interface for the game lobby interactions
 */
interface IGameLobbyRepository {

    /**
     * Register the current user in the game associated to the given id
     * @param gameId id of the game to join
     */
    fun addLocalParticipation(gameId: String)

    /**
     * Create name with specified name and duration
     * @param gameName name of the game to be created
     * @param gameDuration duration in minutes
     */
    fun createGame(gameName: String, gameDuration: Long): String

    /**
     * Get a game's name given its id
     */
    fun getGameName(gameId: String, cb: Callback<String>)

    /**
     * Get a game's duration given its id
     */
    fun getGameDuration(gameId: String, cb: Callback<Long>)

    /**
     * Get a game's list of players given its id
     */
    fun getPlayers(gameId: String, cb: Callback<List<Player>>)

    /**
     * Get a game's list of participations given its id
     */
    fun getParticipations(gameId: String, cb: Callback<List<Participation>>)

    /**
     * Get a game's admin's id given the game id
     */
    fun getAdminId(gameId: String, cb: Callback<String>)

    /**
     * Change the ready attribute of a player
     * @param gameId the id of the game the player is in
     * @param uid the id of the player
     */
    fun changePlayerReady(gameId: String, uid: String)

    /**
     * Sets the ready attribute to a given value
     * @param gameId the id of the game the player is in
     * @param uid the id of the player
     */
    fun setPlayerReady(gameId: String, uid: String, ready: Boolean)

    /**
     * Sets the faction of the player to the given value
     * @param gameId the id of the game the player is in
     * @param uid the id of the player
     */
    fun setPlayerFaction(gameId: String, uid: String, faction: Faction)

    /**
     * Sets the nfc tag of the player to the given value
     * @param gameId the id of the game the player is in
     * @param uid the id of the player
     */
    fun setPlayerTag(gameId: String, uid: String, tag: String)

    /**
     * Remove a participation from a game lobby
     * @param gameId the id of the game
     */
    fun removeLocalParticipation(gameId: String)
}