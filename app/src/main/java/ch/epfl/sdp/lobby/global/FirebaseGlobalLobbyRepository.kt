package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class FirebaseGlobalLobbyRepository : IGlobalLobbyRepository {

    private var fs: FirebaseFirestore = Firebase.firestore

    override fun getAllGames(cb: Callback<List<Game>>) {
        val games: MutableList<Game> = ArrayList()
        fs.collection("games").get().addOnSuccessListener { result ->
            for(doc in result) {
                println(doc.id)
                val g = Game(
                        (doc["id"] as Long).toInt(),
                        doc["name"] as String,
                        doc["admin"] as String,
                        (doc["duration"] as Long),
                        emptyMap(),
                        GameState.valueOf(doc["state"] as String),
                        emptyList(),            // TODO: Retrieve participation list
                        Date(), Date(), Date()  // TODO: Convert timestamps to date
                )
                games.add(g)
            }
            cb(games)
        }
    }


}