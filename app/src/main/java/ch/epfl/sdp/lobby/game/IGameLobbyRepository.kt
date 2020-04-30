package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
import java.sql.Time

interface IGameLobbyRepository {
    /**
     * Create a new game with a given name and duration
     * @param gameName String: the new game name
     * @param gameDuration Time: the game's duration
     * @return Int: The new game's ID
     */
    fun createGame(gameName: String, gameDuration: Time): Int

    /**
     * Give the game's ID
     * @param cb Callback<Int>: a callback called with the game's ID
     */
    fun getGameId(cb: Callback<Int>)

    /**
     * Give the game's name
     * @param cb Callback<Int>: a callback called with the game's name
     */
    fun getGameName(cb: Callback<String>)

    /**
     * Give the game's duration
     * @param cb Callback<Int>: a callback called with the game duration
     */
    fun getGameDuration(cb: Callback<Int>)

    /**
     * Give the list of players in a game
     * @param cb Callback<List<Player>>: a callback called with the list of players
     */
    fun getPlayers(cb: Callback<List<Player>>)

    /**
     * Give the list of participants in a game
     * @param cb Callback<List<Participation>>: a callback called with the list of participants
     */
    fun getParticipations(cb: Callback<List<Participation>>)

    /**
     * Give the game admin's UID
     * @param cb Callback<String>: a callback called with the admin's UID
     */
    fun getAdminId(cb: Callback<String>)

    /**
     * Switch the ready state of a player
     * @param uid String: the player UID
     */
    fun changePlayerReady(uid: String)

    /**
     * Set the ready state of a player
     * @param uid String: the player UID
     * @param ready Boolean: the player's ready state. True = ready, False = not ready
     */
    fun setPlayerReady(uid: String, ready: Boolean)

    /**
     * Set the faction of a player
     * @param uid String: the player UID
     * @param faction String: the player's faction
     */
    fun setPlayerFaction(uid: String, faction: PlayerFaction)

    /**
     * Set the NFC tag of a player
     * @param uid String: the player UID
     * @param tag String: the player's tag
     */
    fun setPlayerTag(uid: String, tag: String)

    /**
     * Add an existing player to a game
     * @param uid String: the player UID
     * @param username String: the player username
     * @return String: the player UID
     */
    fun addPlayer(uid: String, username: String) : String

    /**
     * Remove a player from a game
     * @param uid String: the player UID
     */
    fun removePlayer(uid: String)

    /**
     * Create a new player with a given name
     * @param username String: the new of the new player
     * @return Int: the new player UID
     */
    fun createPlayer(username: String) : String
}