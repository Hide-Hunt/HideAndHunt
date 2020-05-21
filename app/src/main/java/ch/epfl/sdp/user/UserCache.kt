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
    private val cacheFilename = "user_cache"
    private val imageFilename = "user_image"

    /**
     * Invalidate the cache if it currently exists, otherwise does nothing
     * @param context Context: The [Context] from which the call is operated
     */
    fun invalidateCache(context: Context) {
        if (doesExist(context))
            context.deleteFile(cacheFilename)
        if(retrieveProfilePic(context) == null)
            context.deleteFile(imageFilename)
    }

    /**
     * Check if the cache exists on the phone
     * @param context Context: The [Context] from which the call is operated
     * @return Boolean: True if the cache exists, False otherwise
     */
    fun doesExist(context: Context): Boolean {
        return File(cacheFilename).exists()
    }

    /**
     * Check if a profile pic is cached, returns the corresponding bitmap if it exists or null otherwise
     * @param context Context: The [Context] from which the call is operated
     * @return Bitmap?: The Bitmap of the profile pic cached, or null if none is found
     */
    private fun retrieveProfilePic(context: Context): Bitmap? {
        if(!File(imageFilename).exists())
            return null
        return try {
            BitmapFactory.decodeFile(context.filesDir.absolutePath + "/" + imageFilename)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Retrieve the data stored in the cache. The retrieved data are stored in the [LocalUser]
     * @param context Context: The [Context] from which the call is operated
     */
    fun get(context: Context) {
        if (!doesExist(context)) {
            LocalUser.connected = false
            return
        }

        var state = 0
        val reader = BufferedReader(InputStreamReader(context.openFileInput(cacheFilename)))
        var line: String? = reader.readLine()
        while (line != null) {
            when (line.trim()) {
                "DATE" -> state = 0
                "UID" -> state = 1
                "PSEUDO" -> state = 2
                else -> when (state) {
                    0 -> {
                        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale("fr-fr"))
                        val currentTime = Calendar.getInstance()
                        currentTime.add(Calendar.MINUTE, -1)

                        if (currentTime.time.after(sdf.parse(line))) {
                            context.deleteFile(cacheFilename)
                            context.deleteFile(imageFilename)
                            return
                        }
                    }
                    1 -> LocalUser.uid = line.trim()
                    2 -> LocalUser.pseudo = line.trim()
                }
            }
            line = reader.readLine()
        }
        LocalUser.connected = true
        LocalUser.profilePic = retrieveProfilePic(context)
    }

    /**
     * Put new data in the cache. The data is automatically retrieved from the [LocalUser]
     * @param context Context: The [Context] from which the call is operated
     */
    fun put(context: Context) {
        val outputStream = OutputStreamWriter(context.openFileOutput(cacheFilename, Context.MODE_PRIVATE))
        val currentTime = Calendar.getInstance().time
        val output = "DATE\n" + currentTime.toString() + "\nUID\n" + LocalUser.uid + "\nPSEUDO\n" + LocalUser.pseudo + "\n"
        outputStream.write(output, 0, output.length)
        outputStream.flush()
        outputStream.close()

        LocalUser.profilePic?.let { profilePic ->
            val imageStream = context.openFileOutput(imageFilename, Context.MODE_PRIVATE)
            val byteStream = ByteArrayOutputStream()
            profilePic.compress(Bitmap.CompressFormat.PNG, 90, byteStream)
            imageStream.write(byteStream.toByteArray())
            imageStream.flush()
            imageStream.close()
            byteStream.close()
        }
    }
}
