package ch.epfl.sdp.user

import ch.epfl.sdp.authentication.User
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.util.*

class UserCache {
    private val cacheFilename = "cache"

    fun doesExist(): Boolean {
        try{
            val inputStream = FileInputStream(cacheFilename)
            inputStream.close()
        }
        catch(e: Exception) {
            return false
        }
        return true
    }

    fun put () {
        val outputStream = FileOutputStream(cacheFilename, false);
        val currentTime = Calendar.getInstance().time
        outputStream.write("DATE\n".toByteArray(Charset.defaultCharset()))
        outputStream.write(currentTime.toString().toByteArray(Charset.defaultCharset()))
        outputStream.write("IMAGE\n".toByteArray(Charset.defaultCharset()))
        outputStream.write("PLACEHOLDER\n".toByteArray(Charset.defaultCharset()))
        outputStream.write("PSEUDO\n".toByteArray(Charset.defaultCharset()))
        outputStream.write(User.pseudo.toByteArray(Charset.defaultCharset()))
        outputStream.flush();
        outputStream.close();
    }
}