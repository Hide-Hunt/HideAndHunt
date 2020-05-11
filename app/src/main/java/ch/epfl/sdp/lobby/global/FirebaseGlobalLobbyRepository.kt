package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.FirebaseConstants.GAME_COLLECTION
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

/**
 * Repository for Firestore database interactions with the global lobby
 */
class FirebaseGlobalLobbyRepository : IGlobalLobbyRepository {
    private var fs: FirebaseFirestore = Firebase.firestore

    override fun getAllGames(cb: Callback<List<Game>>) {
        fs.collection(GAME_COLLECTION).whereEqualTo("state", GameState.LOBBY).get().addOnSuccessListener { result ->
            cb(result.map { it.toObject<Game>() })
        }
    }
}