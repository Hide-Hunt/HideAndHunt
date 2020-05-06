package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player

class FirebaseGameLobbyRepository : IGameLobbyRepository {

    override fun createGame(gameName: String, gameDuration: Long): Int {
        TODO("Not yet implemented")
    }

    override fun getGameId(cb: Callback<Int>) {
        TODO("Not yet implemented")
    }

    override fun getGameName(cb: Callback<String>) {
        TODO("Not yet implemented")
    }

    override fun getGameDuration(cb: Callback<Int>) {
        TODO("Not yet implemented")
    }

    override fun getPlayers(cb: Callback<List<Player>>) {
        TODO("Not yet implemented")
    }

    override fun getParticipations(cb: Callback<List<Participation>>) {
        TODO("Not yet implemented")
    }

    override fun getAdminId(cb: Callback<Int>) {
        TODO("Not yet implemented")
    }

    override fun changePlayerReady(uid: Int) {
        TODO("Not yet implemented")
    }

    override fun setPlayerReady(uid: Int, ready: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setPlayerFaction(uid: Int, faction: PlayerFaction) {
        TODO("Not yet implemented")
    }

    override fun setPlayerTag(uid: Int, tag: String) {
        TODO("Not yet implemented")
    }
}