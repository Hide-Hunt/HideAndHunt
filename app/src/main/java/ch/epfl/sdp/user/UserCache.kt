package ch.epfl.sdp.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ch.epfl.sdp.authentication.LocalUser
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.io.File
import java.util.*

/**
 * Cache tu store user information on the phone
 */
class UserCache {
    companion object {
        private const val CACHE_EXPIRATION = 3600
        private const val CACHE_FILENAME = "user_cache"
        private const val IMAGE_FILENAME = "user_image"
    }

    /**
     * Invalidate the cache if it currently exists, otherwise does nothing
     * @param context Context: The [Context] from which the call is operated
     */
    fun invalidateCache(context: Context) {
        File(context.cacheDir, CACHE_FILENAME).let {
            if(it.exists()) it.delete()
        }
        File(context.cacheDir, IMAGE_FILENAME).let {
            if(it.exists()) it.delete()
        }
    }

    /**
     * Check whether the cache currently exists
     * @param context Context: The [Context] from which the call is operated
     * @return Boolean: true if the cache is set
     */
    fun doesExist(context: Context): Boolean {
        return File(context.cacheDir, CACHE_FILENAME).exists()
    }

    /**s
     * Check if a profile pic is cached, returns the corresponding bitmap if it exists or null otherwise
     * @param context Context: The [Context] from which the call is operated
     * @return Bitmap?: The Bitmap of the profile pic cached, or null if none is found
     */
    private fun retrieveProfilePic(context: Context): Bitmap? {
        File(context.cacheDir, IMAGE_FILENAME).let {
            return if(it.exists()) BitmapFactory.decodeFile(context.cacheDir.absolutePath + "/" + IMAGE_FILENAME)
            else null
        }
    }

    /**
     * Retrieve the data stored in the cache. The retrieved data are stored in the [LocalUser]
     * @param context Context: The [Context] from which the call is operated
     */
    fun get(context: Context) {
        val cacheFile = File(context.cacheDir, CACHE_FILENAME)
        if(!cacheFile.exists()) {
            LocalUser.connected = false
            return
        }

        val currentTime = System.currentTimeMillis() / 1000
        val lines = cacheFile.readLines()
        val timestampLimit = lines[0].toLong()

        if(timestampLimit + CACHE_EXPIRATION < currentTime) {
            invalidateCache(context)
            LocalUser.connected = false
            return
        }

        LocalUser.uid = lines[1].trim()
        LocalUser.pseudo = lines[2].trim()
        LocalUser.connected = true
        LocalUser.profilePic = retrieveProfilePic(context)
    }

    /**
     * Put new data in the cache. The data is automatically retrieved from the [LocalUser]
     * @param context Context: The [Context] from which the call is operated
     */
    fun put(context: Context) {
        val currentTime = System.currentTimeMillis() / 1000
        val output = currentTime.toString() + "\n" + LocalUser.uid + "\n" + LocalUser.pseudo
        File(context.cacheDir, CACHE_FILENAME).writeText(output)
    }
}
