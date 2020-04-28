package ch.epfl.sdp.user

import android.content.Context
import android.graphics.Bitmap
import ch.epfl.sdp.authentication.User

class MockUserCache : IUserCache {
    private var pseudo = ""
    private var profilePic: Bitmap? = null
    private var uid = ""
    private var cacheExists = false

    override fun get(context: Context) {
        if(!cacheExists)
            return
        User.pseudo = pseudo
        User.uid = uid
        User.profilePic = profilePic
    }

    override fun put(context: Context) {
        pseudo = User.pseudo
        uid = User.uid
        profilePic = User.profilePic
        cacheExists = true
    }

}