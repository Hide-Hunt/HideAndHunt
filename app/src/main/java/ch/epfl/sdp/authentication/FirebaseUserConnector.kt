package ch.epfl.sdp.authentication

import com.google.firebase.auth.FirebaseAuth

class FirebaseUserConnector : IUserConnector {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun connect(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email.toString(), password.toString())
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val authed = auth.currentUser
                User.username = email
                User.uid = authed!!.uid
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

    override fun register(email: String, password: String): Boolean {
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                val authed = auth.currentUser
                User.connected = true
                User.username = email
                User.uid = authed!!.uid
            } else {
                User.connected = false
            }
        }
        return User.connected
    }

}