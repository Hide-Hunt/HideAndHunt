package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.user.User

interface GameLobbyRepository {
    fun getGame(cb : (Game) -> Unit)

    fun getPlayers(cb : (List<User>) -> Unit)

    fun setPlayerReady(player : User)
}