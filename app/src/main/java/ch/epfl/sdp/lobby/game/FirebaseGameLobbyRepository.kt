package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class FirebaseGameLobbyRepository : IGameLobbyRepository {

    private var fs: FirebaseFirestore = Firebase.firestore

    override fun createGame(gameName: String, gameDuration: Long): Int {
        fs.collection("games").add(
                Game(
                        gameName.hashCode(),
                        gameName,
                        LocalUser.username,
                        gameDuration,
                        emptyMap(), //TODO: For now no params
                        GameState.LOBBY,
                        emptyList(), //TODO: Add local user participation
                        Date(),
                        Date(Long.MAX_VALUE), //TODO: For now the game is available to the max
                        Date()
                )
        )
        return gameName.hashCode() //TODO: Better id generation
    }

    override fun getGameName(gameId: Int, cb: Callback<String>) {
        TODO("Not yet implemented")
    }

    override fun getGameDuration(gameId: Int, cb: Callback<Int>) {
        TODO("Not yet implemented")
    }

    override fun getPlayers(gameId: Int, cb: Callback<List<Player>>) {
        TODO("Not yet implemented")
    }

    override fun getParticipations(gameId: Int, cb: Callback<List<Participation>>) {
        TODO("Not yet implemented")
    }

    override fun getAdminId(gameId: Int, cb: Callback<Int>) {
        TODO("Not yet implemented")
    }

    override fun changePlayerReady(gameId: Int, uid: Int) {
        TODO("Not yet implemented")
    }

    override fun setPlayerReady(gameId: Int, uid: Int, ready: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setPlayerFaction(gameId: Int, uid: Int, faction: PlayerFaction) {
        TODO("Not yet implemented")
    }

    override fun setPlayerTag(gameId: Int, uid: Int, tag: String) {
        TODO("Not yet implemented")
    }
}