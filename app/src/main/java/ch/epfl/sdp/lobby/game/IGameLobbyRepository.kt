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
    fun addLocalParticipation(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback)

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
     * @param successCallback Callback<String>: callback operating on the game name
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun getGameName(gameId: String, successCallback: Callback<String>, failureCallback: UnitCallback)

    /**
     * Get a game's duration given its id
     * @param gameId Int: id of the game
     * @param successCallback Callback<Long>: callback operating on the game duration
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun getGameDuration(gameId: String, successCallback: Callback<Long>, failureCallback: UnitCallback)

    /**
     * Get a game's list of players given its id
     * @param gameId Int: id of the game
     * @param successCallback Callback<List<Player>>: callback operating on the list of players
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun getPlayers(gameId: String, successCallback: Callback<List<Player>>, failureCallback: UnitCallback)

    /**
     * Get a game's list of participations given its id
     * @param gameId Int: id of the game
     * @param successCallback Callback<List<Participation>>: callback operating on the list of participations
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun getParticipations(gameId: String, successCallback: Callback<List<Participation>>, failureCallback: UnitCallback)

    /**
     * Get a game's admin's id given the game id
     * @param gameId Int: id of the game
     * @param successCallback Callback<Int>: callback operating on the list of admin's ids
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun getAdminId(gameId: String, successCallback: Callback<String>, failureCallback: UnitCallback)

    /**
     * Requests the game to be launched on the server
     * @param gameId Int: the id of the game to start
     * @param successCallback UnitCallback: callback to be called when done
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun requestGameLaunch(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback)

    /**
     * Registers a listener for a game start
     * @param gameId Int: the id of the game to monitor
     * @param listener OnGameStartListener: the object to notify on game start
     */
    fun setOnGameStartListener(gameId: String, listener: OnGameStartListener?)

    /**
     * Sets the ready attribute to a given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param ready Boolean: the new state of the attribute
     * @param successCallback UnitCallback: The callback function to call when done changing player
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun setPlayerReady(gameId: String, uid: String, ready: Boolean, successCallback: UnitCallback, failureCallback: UnitCallback)

    /**
     * Sets the faction of the player to the given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param faction PlayerFaction: the new player's faction
     * @param successCallback UnitCallback: The callback function to call when done changing player
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun setPlayerFaction(gameId: String, uid: String, faction: Faction, successCallback: UnitCallback, failureCallback: UnitCallback)

    /**
     * Sets the nfc tag of the player to the given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param tag String: the new player's tag
     * @param successCallback UnitCallback: The callback function to call when done changing player
     * @param failureCallback UnitCallback: callback in case of failure
     */
    fun setPlayerTag(gameId: String, uid: String, tag: String, successCallback: UnitCallback, failureCallback: UnitCallback)

    /**
     * Remove a participation from a game lobby
     * @param gameId Int: the id of the game
     */
    fun removeLocalParticipation(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback)
}