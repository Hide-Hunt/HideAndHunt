package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.user.User

interface GameLobbyRepository {

    fun gitGameId(cb : (Int) -> Unit)

    fun getGameName(cb : (String) -> Unit)

    fun getGameDuration(cb : (Int) -> Unit)

    fun getPlayers(cb : (List<Participation>) -> Unit)

    fun setPlayerReady(player : User)
}