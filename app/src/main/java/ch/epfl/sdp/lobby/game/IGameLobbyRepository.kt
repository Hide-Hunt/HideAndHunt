package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player

/**
 * A base interface for the game lobby interactions
 */
interface IGameLobbyRepository {

    /**
     * Register the current user in the game associated to the given id
     * @param gameId Int: id of the game to join
     */
    fun addLocalParticipation(gameId: Int)

    /**
     * Create name with specified name and duration
     * @param gameName String: name of the game to be created
     * @param gameDuration Long: duration in minutes
     * @return Int: the new game id
     */
    fun createGame(gameName: String, gameDuration: Long): Int

    /**
     * Get a game's name given its id
     * @param gameId Int: id of the game
     * @param cb Callback<String>: callback operating on the game name
     */
    fun getGameName(gameId: Int, cb: Callback<String>)

    /**
     * Get a game's duration given its id
     * @param gameId Int: id of the game
     * @param cb Callback<Long>: callback operating on the game duration
     */
    fun getGameDuration(gameId: Int, cb: Callback<Long>)

    /**
     * Get a game's list of players given its id
     * @param gameId Int: id of the game
     * @param cb Callback<List<Player>>: callback operating on the list of players
     */
    fun getPlayers(gameId: Int, cb: Callback<List<Player>>)

    /**
     * Get a game's list of participations given its id
     * @param gameId Int: id of the game
     * @param cb Callback<List<Participation>>: callback operating on the list of participations
     */
    fun getParticipations(gameId: Int, cb: Callback<List<Participation>>)

    /**
     * Get a game's admin's id given the game id
     * @param gameId Int: id of the game
     * @param cb Callback<Int>: callback operating on the list of admin's ids
     */
    fun getAdminId(gameId: Int, cb: Callback<Int>)

    /**
     * Change the ready attribute of a player
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     */
    fun changePlayerReady(gameId: Int, uid: Int)

    /**
     * Sets the ready attribute to a given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param ready Boolean: the new state of the attribute
     */
    fun setPlayerReady(gameId: Int, uid: Int, ready: Boolean)

    /**
     * Sets the faction of the player to the given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param faction PlayerFaction: the new player's faction
     */
    fun setPlayerFaction(gameId: Int, uid: Int, faction: PlayerFaction)

    /**
     * Sets the nfc tag of the player to the given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param tag String: the new player's tag
     */
    fun setPlayerTag(gameId: Int, uid: Int, tag: String)

    /**
     * Remove a participation from a game lobby
     * @param gameId Int: the id of the game
     */
    fun removeLocalParticipation(gameId: Int)
}