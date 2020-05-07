package ch.epfl.sdp.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import ch.epfl.sdp.authentication.User
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class UserCache {
    private val cacheFilename = "user_cache"
    private val imageFilename = "user_image"

    fun invalidateCache(context: Context) {
        if(doesExist(context)) {
            context.deleteFile(cacheFilename)
            context.deleteFile(imageFilename)
        }
    }

    private fun doesExist(context: Context): Boolean {
        try{
            val inputStream = InputStreamReader(context.openFileInput(cacheFilename))
            inputStream.close()
            val imageStream = InputStreamReader(context.openFileInput(imageFilename))
            imageStream.close()
        }
        catch(e: Exception) {
            return false
        }
        return true
    }

    fun get(context: Context) {
        if(!doesExist(context)) {
           // context.filesDir.list().forEach { Log.d("CACHE", it) }
            Log.d("CACHE", "USER NOT FOUND IN CACHE")
            User.connected = false
            return
        }

        var state = 0
        val reader = BufferedReader(InputStreamReader(context.openFileInput(cacheFilename)))
        var line: String? = reader.readLine()
        while (line != null) {
            when(line.trim()) {
                "DATE" -> state = 0
                "UID" -> state = 1
                "PSEUDO" -> state = 2
                else -> when(state) {
                    0 -> {
                        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale("fr-fr"))
                        val currentTime = Calendar.getInstance()
                        currentTime.add(Calendar.MINUTE, -1)

                        if(currentTime.time.after(sdf.parse(line))) {
                            Log.d("CACHE", "CACHE IS EXPIRED, DELETING")
                            context.deleteFile(cacheFilename)
                            context.deleteFile(imageFilename)
                            return
                        }
                    }
                    1 -> User.uid = line.trim()
                    2 -> User.pseudo = line.trim()
                }
            }
            line = reader.readLine()
        }
        User.connected = true
        User.profilePic = BitmapFactory.decodeFile(context.filesDir.absolutePath + "/" + imageFilename)
        Log.d("CACHE", "USER FOUND IN CACHE")
    }

    fun put (context: Context) {
        val outputStream = OutputStreamWriter(context.openFileOutput(cacheFilename, Context.MODE_PRIVATE))
        val currentTime = Calendar.getInstance().time
        val output = "DATE\n" + currentTime.toString() + "\nUID\n" + User.uid + "\nPSEUDO\n" + User.pseudo + "\n"
        outputStream.write(output, 0, output.length)
        outputStream.flush()
        outputStream.close()

        val imageStream = context.openFileOutput(imageFilename, Context.MODE_PRIVATE)
        val byteStream = ByteArrayOutputStream()
        User.profilePic!!.compress(Bitmap.CompressFormat.PNG, 90, byteStream)
        imageStream.write(byteStream.toByteArray())
        imageStream.flush()
        imageStream.close()
        byteStream.close()
        Log.d("CACHE", "MODIFICATIONS PUT TO CACHE")
    }
}