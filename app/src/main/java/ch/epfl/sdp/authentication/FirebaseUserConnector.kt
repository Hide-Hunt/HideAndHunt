package ch.epfl.sdp.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseUserConnector : IUserConnector {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun connect(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email.toString(), password.toString())
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val authed = auth.currentUser
                user.email = email
                user.uid = authed!!.uid
                user.connected = true
            } else {
                user.connected = false
            }
        }
        db.collection("user").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        user.pseudo = document.data!!["pseudo"].toString()
                    } else {
                        user.connected = false
                    }
                }
                .addOnFailureListener { _ ->
                    user.connected = false
                }

        return user.connected
    }

    override fun disconnect(): Boolean {
        auth.signOut()
        user.connected = false
        return true
    }

    override fun register(email: String, password: String, pseudo: String): Boolean {
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                val authed = auth.currentUser
                user.connected = true
                user.email = email
                user.uid = authed!!.uid
                user.pseudo = pseudo
            } else {
                user.connected = false
            }
        }

        val dataToAdd = hashMapOf(
                "pseudo" to pseudo,
                "uid" to user.uid
        )
        db.collection("user")
                .add(dataToAdd)
                //Keep for eventual future use, clean if not needed
                /*.addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }*/
        return user.connected
    }

}