package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.game.data.Participation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

/**
 * Repository for Firestore database interactions with the global lobby
 * Implements [IGlobalLobbyRepository]
 */
class FirebaseGlobalLobbyRepository : IGlobalLobbyRepository {

    private var fs: FirebaseFirestore = Firebase.firestore

    override fun getAllGames(cb: Callback<List<Game>>) {
        val games: MutableList<Game> = ArrayList()
        fs.collection("games").get().addOnSuccessListener { result ->
            for ((i, doc) in result.withIndex()) {
                getParticipations((doc["id"] as Long).toInt()) { participations ->
                    val g = Game(
                            (doc["id"] as Long).toInt(),
                            doc["name"] as String,
                            doc["admin"] as String,
                            (doc["duration"] as Long),
                            emptyMap(),
                            GameState.valueOf(doc["state"] as String),
                            participations,
                            Date(), Date(), Date(),  // TODO: Convert timestamps to date
                            (doc["adminID"] as Long).toInt()
                    )
                    games.add(g)
                    if (i == result.size() - 1) //The last game calls the callback
                        cb(games.filter { p -> p.state == GameState.LOBBY })
                }
            }
            if (result.size() == 0) {
                // no games found
                cb(emptyList())
            }
        }
    }

    fun getParticipations(gameId: Int, cb: Callback<List<Participation>>) {
        val players: MutableList<Participation> = java.util.ArrayList()
        fs.collection("participations").whereEqualTo("gameID", gameId).get().addOnSuccessListener { documents ->

            for (doc in documents) {
                if (players.none { p -> p.playerID == (doc["playerID"] as Long).toInt() }) {
                    players.add(Participation(
                            doc["username"] as String,
                            doc["ready"] as Boolean,
                            doc["tag"] as String,
                            (doc["playerID"] as Long).toInt(),
                            PlayerFaction.valueOf(doc["faction"] as String),
                            (doc["gameID"] as Long).toInt()
                    ))
                }
            }
            cb(players)
        }
    }
}