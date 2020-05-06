package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
import java.sql.Time

interface IGameLobbyRepository {
    fun createGame(gameName: String, gameDuration: Long): Int

    fun getGameName(gameId: Int, cb: Callback<String>)

    fun getGameDuration(gameId: Int, cb: Callback<Long>)

    fun getPlayers(gameId: Int, cb: Callback<List<Player>>)

    fun getParticipations(gameId: Int, cb: Callback<List<Participation>>)

    fun getAdminId(gameId: Int, cb: Callback<Int>)

    fun changePlayerReady(gameId: Int, uid: Int)

    fun setPlayerReady(gameId: Int, uid: Int, ready: Boolean)

    fun setPlayerFaction(gameId: Int, uid: Int, faction: PlayerFaction)

    fun setPlayerTag(gameId: Int, uid: Int, tag: String)
}