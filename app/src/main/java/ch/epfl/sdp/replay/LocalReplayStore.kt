package ch.epfl.sdp.replay

import android.content.Context
import java.io.File

class LocalReplayStore(private val context: Context) {
    fun getPath(id: String): String = context.filesDir.absolutePath + "/replays/" + id

    fun getFile(id: String): File = File(getPath(id))
}