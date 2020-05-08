package ch.epfl.sdp.authentication

import android.graphics.Bitmap

object LocalUser {
    lateinit var pseudo: String
    lateinit var email: String
    lateinit var uid: String
    var profilePic: Bitmap? = null
    @Volatile var connected = false
}