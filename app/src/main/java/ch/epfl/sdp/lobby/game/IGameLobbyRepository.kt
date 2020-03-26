package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player

interface IGameLobbyRepository {


    fun getGameId(cb : Callback<Int>)

    fun getGameName(cb : Callback<String>)

    fun getGameDuration(cb : Callback<Int>)

    fun getPlayers(cb : Callback<List<Player>>)

    fun getParticipations(cb : Callback<List<Participation>>)

    fun getAdminId(cb : Callback<Int>)

    fun changePlayerReady(uid : Int)

    fun setPlayerFaction(uid : Int, faction : PlayerFaction)
}