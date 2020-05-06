package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.IDHelper
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Document
import java.util.*

class FirebaseGameLobbyRepository : IGameLobbyRepository {

    private var fs: FirebaseFirestore = Firebase.firestore

    private fun getFirebaseGameWithID(gameID: Int, cb: Callback<String>) {
        val games: MutableList<Game> = ArrayList()
        fs.collection("games").get().addOnSuccessListener { result ->
            val match = result.filter { x -> (x["id"] as Int) == gameID }
            if(match.isEmpty()) {
                cb("")
            } else {
                cb(match[0].id)
            }
        }
    }

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
        getFirebaseGameWithID(gameId) { fid ->
            if(fid != "") {
                fs.collection("games").document(fid).get().addOnSuccessListener {
                    cb(it["name"] as String)
                }
            }
        }
    }

    override fun getGameDuration(gameId: Int, cb: Callback<Long>) {
        getFirebaseGameWithID(gameId) { fid ->
            if(fid != "") {
                fs.collection("games").document(fid).get().addOnSuccessListener {
                    cb(it["duration"] as Long)
                }
            }
        }
    }

    override fun getPlayers(gameId: Int, cb: Callback<List<Player>>) {
        getParticipations(gameId) {
            cb(it.map { x -> Player(x.playerID) })
        }
    }

    override fun getParticipations(gameId: Int, cb: Callback<List<Participation>>) {
        val players: MutableList<Participation> = ArrayList()
        getFirebaseGameWithID(gameId) { fid ->
            if(fid != "") {
                fs.collection("games").document(fid).get().addOnSuccessListener { game ->
                    val participations = (game["participation"] as List<*>).filterIsInstance<DocumentReference>()
                    participations.forEach { part ->
                        part.get().addOnSuccessListener {
                            players.add(
                                    Participation(
                                            (it["state"] as Int) == 0,
                                            it["tag"] as String,
                                            it["playerID"] as Int,
                                            PlayerFaction.values()[it["faction"] as Int]
                                    )
                            )
                        }
                    }
                }
            }
        }
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