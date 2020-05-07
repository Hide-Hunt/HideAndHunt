package ch.epfl.sdp.user

import android.content.Context
import android.graphics.Bitmap
import ch.epfl.sdp.authentication.User

object MockUserCache : IUserCache {
    private var pseudo = ""
    private var profilePic: Bitmap? = null
    private var uid = ""
    private var cacheExists = false

    fun resetCache() {
        pseudo = ""
        profilePic = null
        uid = ""
        cacheExists = false
    }

    fun fakeCache() {
        cacheExists = true
    }

    override fun get(context: Context) {
        if(!cacheExists) {
            User.connected = false
            return
        }
        User.pseudo = pseudo
        User.uid = uid
        User.profilePic = profilePic
        User.connected = true
    }

    override fun put(context: Context) {
        pseudo = User.pseudo
        uid = User.uid
        profilePic = User.profilePic
        cacheExists = true
    }

    override fun toString(): String {
        return User.pseudo + ":" + User.uid
    }

}