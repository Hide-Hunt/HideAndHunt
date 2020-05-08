package ch.epfl.sdp.authentication

import com.google.firebase.auth.FirebaseAuth

class FirebaseUserConnector : IUserConnector {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun connect(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email.toString(), password.toString())
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val authed = auth.currentUser
                LocalUser.username = email
                LocalUser.uid = authed!!.uid
                LocalUser.connected = true
            } else {
                LocalUser.connected = false
            }
        }

        return LocalUser.connected
    }

    override fun disconnect(): Boolean {
        auth.signOut()
        LocalUser.connected = false
        return true
    }

    override fun register(email: String, password: String): Boolean {
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                val authed = auth.currentUser
                LocalUser.connected = true
                LocalUser.username = email
                LocalUser.uid = authed!!.uid
            } else {
                LocalUser.connected = false
            }
        }
        return LocalUser.connected
    }

}