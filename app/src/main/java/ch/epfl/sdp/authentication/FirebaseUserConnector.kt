package ch.epfl.sdp.authentication

import com.google.firebase.auth.FirebaseAuth

class FirebaseUserConnector : IUserConnector {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun connect(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email.toString(), password.toString())
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val authed = auth.currentUser
                user.username = email
                user.uid = authed!!.uid
                user.connected = true
            } else {
                user.connected = false
            }
        }

        return user.connected
    }

    override fun disconnect(): Boolean {
        auth.signOut()
        user.connected = false
        return true
    }

    override fun register(email: String, password: String): Boolean {
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                val authed = auth.currentUser
                user.connected = true
                user.username = email
                user.uid = authed!!.uid
            } else {
                user.connected = false
            }
        }
        return user.connected
    }

}