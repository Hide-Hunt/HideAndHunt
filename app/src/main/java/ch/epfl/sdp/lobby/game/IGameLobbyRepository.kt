package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.UnitCallback
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player

/**
 * A base interface for the game lobby interactions
 */
interface IGameLobbyRepository {
    interface OnGameStartListener {
        fun onGameStart()
    }

    /**
     * Register the current user in the game associated to the given id
     * @param gameId Int: id of the game to join
     */
    fun addLocalParticipation(gameId: String)

    /**
     * Create name with specified name and duration
     * @param gameName String: name of the game to be created
     * @param gameDuration Long: duration in minutes
     * @return Int: the new game id
     */
    fun createGame(gameName: String, gameDuration: Long): String

    /**
     * Get a game's name given its id
     * @param gameId Int: id of the game
     * @param cb Callback<String>: callback operating on the game name
     */
    fun getGameName(gameId: String, cb: Callback<String>)

    /**
     * Get a game's duration given its id
     * @param gameId Int: id of the game
     * @param cb Callback<Long>: callback operating on the game duration
     */
    fun getGameDuration(gameId: String, cb: Callback<Long>)

    /**
     * Get a game's list of players given its id
     * @param gameId Int: id of the game
     * @param cb Callback<List<Player>>: callback operating on the list of players
     */
    fun getPlayers(gameId: String, cb: Callback<List<Player>>)

    /**
     * Get a game's list of participations given its id
     * @param gameId Int: id of the game
     * @param cb Callback<List<Participation>>: callback operating on the list of participations
     */
    fun getParticipations(gameId: String, cb: Callback<List<Participation>>)

    /**
     * Get a game's admin's id given the game id
     * @param gameId Int: id of the game
     * @param cb Callback<Int>: callback operating on the list of admin's ids
     */
    fun getAdminId(gameId: String, cb: Callback<String>)

    /**
     * Change the ready attribute of a player
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param cb UnitCallback: The callback function to call when done changing player
     */
    fun changePlayerReady(gameId: String, uid: String, cb: UnitCallback)

    /**
     * Requests the game to be launched on the server
     * @param gameId Int: the id of the game to start
     */
    fun requestGameLaunch(gameId: String)

    /**
     * Registers a listener for a game start
     * @param gameId Int: the id of the game to monitor
     * @param listener OnGameStartListener: the object to notify on game start
     */
    fun setOnGameStartListener(gameId: String, listener: OnGameStartListener)

    /**
     * Sets the ready attribute to a given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param ready Boolean: the new state of the attribute
     * @param cb UnitCallback: The callback function to call when done changing player
     */
    fun setPlayerReady(gameId: String, uid: String, ready: Boolean, cb: UnitCallback)

    /**
     * Sets the faction of the player to the given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param faction PlayerFaction: the new player's faction
     * @param cb UnitCallback: The callback function to call when done changing player
     */
    fun setPlayerFaction(gameId: String, uid: String, faction: Faction, cb: UnitCallback)

    /**
     * Sets the nfc tag of the player to the given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param tag String: the new player's tag
     * @param cb UnitCallback: The callback function to call when done changing player
     */
    fun setPlayerTag(gameId: String, uid: String, tag: String, cb: UnitCallback)

    /**
     * Remove a participation from a game lobby
     * @param gameId Int: the id of the game
     */
    fun removeLocalParticipation(gameId: String)
}