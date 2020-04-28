package ch.epfl.sdp.user

import android.content.Context
import android.graphics.Bitmap

interface IUserCache {
    fun get(context: Context)
    fun put (context: Context)
}