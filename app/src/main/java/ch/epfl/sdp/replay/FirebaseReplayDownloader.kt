package ch.epfl.sdp.replay

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.UnitCallback
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class FirebaseReplayDownloader: IReplayDownloader {
    private var storage = FirebaseStorage.getInstance()
    private var storageRef = storage
            .getReferenceFromUrl("gs://hidehunt-71e41.appspot.com")
            .child("replays")

    override fun download(gameID: String,
                          file: File,
                          successCallback: UnitCallback,
                          failureCallback: Callback<String>): IReplayDownloader.IReplayDownload {
        var canceled = false

        val downloadHandle = storageRef.child("$gameID.game").getFile(file)
                .addOnSuccessListener {
                    if (canceled) { failureCallback("Canceled") }
                    else { successCallback() }
                }
                .addOnFailureListener { failureCallback(it.toString()) }
                .addOnCanceledListener { failureCallback("Canceled") }

        return object : IReplayDownloader.IReplayDownload {
            override fun cancel() {
                canceled = true
                downloadHandle.cancel()
            }
        }
    }
}