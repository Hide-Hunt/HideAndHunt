package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.SuccFailCallbacks.*
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
     * @param cb UnitCallback: UnitSuccFailCallback pair of success/failure unit callbacks
     */
    fun addLocalParticipation(gameId: String, cb: UnitSuccFailCallback)

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
     * @param cb UnitCallback: SuccFailCallback<String> pair of success/failure callbacks
     */
    fun getGameName(gameId: String, cb: SuccFailCallback<String>)

    /**
     * Get a game's duration given its id
     * @param gameId Int: id of the game
     * @param cb UnitCallback: SuccFailCallback<Long> pair of success/failure callbacks
     */
    fun getGameDuration(gameId: String, cb: SuccFailCallback<Long>)

    /**
     * Get a game's list of players given its id
     * @param gameId Int: id of the game
     * @param cb UnitCallback: SuccFailCallback<List<Player>> pair of success/failure callbacks
     */
    fun getPlayers(gameId: String, cb: SuccFailCallback<List<Player>>)

    /**
     * Get a game's list of participation given its id
     * @param gameId Int: id of the game
     * @param cb UnitCallback: SuccFailCallback<List<Participation>> pair of success/failure callbacks
     */
    fun getParticipation(gameId: String, cb: SuccFailCallback<List<Participation>>)

    /**
     * Get a game's admin's id given the game id
     * @param gameId Int: id of the game
     * @param cb UnitCallback: SuccFailCallback<String> pair of success/failure callbacks
     */
    fun getAdminId(gameId: String, cb: SuccFailCallback<String>)

    /**
     * Requests the game to be launched on the server
     * @param gameId Int: the id of the game to start
     * @param cb UnitCallback: UnitSuccFailCallback pair of success/failure unit callbacks
     */
    fun requestGameLaunch(gameId: String, cb: UnitSuccFailCallback)

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
     * @param cb UnitCallback: UnitSuccFailCallback pair of success/failure unit callbacks
     */
    fun setPlayerReady(gameId: String, uid: String, ready: Boolean, cb: UnitSuccFailCallback)

    /**
     * Sets the faction of the player to the given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param faction PlayerFaction: the new player's faction
     * @param cb UnitCallback: UnitSuccFailCallback pair of success/failure unit callbacks
     */
    fun setPlayerFaction(gameId: String, uid: String, faction: Faction, cb: UnitSuccFailCallback)

    /**
     * Sets the nfc tag of the player to the given value
     * @param gameId Int: the id of the game the player is in
     * @param uid Int: the id of the player
     * @param tag String: the new player's tag
     * @param cb UnitCallback: UnitSuccFailCallback pair of success/failure unit callbacks
     */
    fun setPlayerTag(gameId: String, uid: String, tag: String, cb: UnitSuccFailCallback)

    /**
     * Remove a participation from a game lobby
     * @param gameId Int: the id of the game
     */
    fun removeLocalParticipation(gameId: String, cb: UnitSuccFailCallback)
}