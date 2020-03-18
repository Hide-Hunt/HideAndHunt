package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.User

interface IGameLobbyRepository {


    fun getGameId(cb : (Int) -> Unit)

    fun getGameName(cb : (String) -> Unit)

    fun getGameDuration(cb : (Int) -> Unit)

    fun getPlayers(cb : (List<Participation>) -> Unit)

    fun getAdminId(cb : (Int) -> Unit)

    fun changePlayerReady(user: User)

    fun setPlayerFaction(uid : Int, faction : PlayerParametersFragment.Faction)
}