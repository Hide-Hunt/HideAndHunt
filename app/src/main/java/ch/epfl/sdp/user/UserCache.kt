package ch.epfl.sdp.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import ch.epfl.sdp.authentication.User
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*


class UserCache {
    private val cacheFilename = "user_cache"

    private fun doesExist(context: Context): Boolean {
        try{
            val inputStream = InputStreamReader(context.openFileInput(cacheFilename))
            inputStream.close()
        }
        catch(e: Exception) {
            return false
        }
        Log.d("CACHE", "FILE EXISTS")
        return true
    }

    fun get(context: Context) {
        if(!doesExist(context)) {
            Log.d("CACHE", "FILE DOES NOT EXIST")
            return
        }
        var state = 0
        val reader = BufferedReader(InputStreamReader(context.openFileInput(cacheFilename)))
        var line: String? = null
        while (line != null) {
            line = reader.readLine()
            when(line.trim()) {
                "DATE" -> state = 0
                "IMAGE" -> state = 1
                "PSEUDO" -> state = 2
                else -> when(state) {
                    0 -> {
                        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale("fr-fr"))
                        val currentTime = Calendar.getInstance().time

                        val c = Calendar.getInstance()
                        c.time = sdf.parse(line!!)!!
                        c.add(Calendar.DATE, 7)
                        if(currentTime.time > c.time.time) {
                            context.deleteFile(cacheFilename)
                            return
                        }
                    }
                    1 -> User.profilePic = BitmapFactory.decodeByteArray(line.toByteArray(), 0, line.toByteArray().size)
                    2 -> User.pseudo = line.trim()
                }
            }
        }
        User.connected = true
    }

    fun put (context: Context) {
        val outputStream = OutputStreamWriter(context.openFileOutput(cacheFilename, Context.MODE_PRIVATE))
        val stream = ByteArrayOutputStream()
        User.profilePic!!.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val currentTime = Calendar.getInstance().time
        val output = "DATE\n" + currentTime.toString() + "\nIMAGE\n" + stream.size().toString() + "\n" + stream.toByteArray() + "\nPSEUDO\n" + User.pseudo + "\n"
        outputStream.write(output, 0, output.length)
        outputStream.flush()
        outputStream.close()
    }
}