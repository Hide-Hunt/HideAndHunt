package ch.epfl.sdp.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseUserConnector : IUserConnector {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun connect(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val authed = auth.currentUser
                User.email = email
                User.uid = authed!!.uid
                db.collection("user").document(User.uid).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                User.pseudo = document.data!!["pseudo"].toString()
                            } else {
                                User.connected = false
                            }
                        }
                        .addOnFailureListener { _ ->
                            User.connected = false
                        }
                User.connected = true
            } else {
                User.connected = false
            }
        }

        return User.connected
    }

    override fun disconnect(): Boolean {
        auth.signOut()
        User.connected = false
        return true
    }

    override fun register(email: String, password: String, pseudo: String): Boolean {
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                val authed = auth.currentUser
                User.email = email
                User.uid = authed!!.uid
                User.pseudo = pseudo
                val dataToAdd = hashMapOf("pseudo" to pseudo)
                db.collection("user").document(User.uid)
                        .set(dataToAdd)
                        .addOnSuccessListener { User.connected = true }
                        .addOnFailureListener { User.connected = false }
            } else {
                User.connected = false
            }
        }
        return User.connected
    }

}