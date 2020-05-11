package ch.epfl.sdp.user

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.FirebaseConstants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseUserRepo : IUserRepo {
    private var fs: FirebaseFirestore = Firebase.firestore

    override fun getUsername(userID: String, cb: Callback<String>) {
        fs.collection(FirebaseConstants.USER_COLLECTION).document(userID).get().addOnSuccessListener { result ->
            cb(result["pseudo"] as String)
        }
    }

    override fun addGameToHistory(userID: String, gameID: String) {
        fs.collection(FirebaseConstants.USER_COLLECTION).document(userID)
                .update(FirebaseConstants.USER_GAME_HISTORY_COLLECTION, FieldValue.arrayUnion(gameID))
    }
}