package ch.epfl.sdp.replay

import android.content.Context
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.UnitCallback
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class FirebaseReplayDownloader(private val context: Context) : IReplayDownloader {
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.getReferenceFromUrl("gs://hidehunt-71e41.appspot.com").child("replays")

    override fun download(gameID: Int, successCallback: UnitCallback, failureCallback: Callback<String>): IReplayDownloader.IReplayDownload {
        var canceled = false
        checkFolder()

        val tempFile = File(context.filesDir.absolutePath + "/replays/game_$gameID.tmp")
        val downloadHandle = storageRef.child("$gameID.game").getFile(tempFile)
                .addOnSuccessListener {
                    if (canceled) {
                        tempFile.delete()
                    } else {
                        tempFile.renameTo(File(context.filesDir.absolutePath + "/replays/$gameID.game"))
                        successCallback()
                    }
                }
                .addOnFailureListener {
                    tempFile.delete()
                    failureCallback(it.toString())
                }
                .addOnCanceledListener {
                    tempFile.delete()
                    failureCallback("Canceled")
                }

        return object : IReplayDownloader.IReplayDownload {
            override fun cancel() {
                canceled = true
                downloadHandle.cancel()
            }
        }
    }

    private fun checkFolder() {
        val folder = File(context.filesDir.absolutePath + "/replays")
        if(!folder.exists()){
            folder.mkdir()
        }
    }
}