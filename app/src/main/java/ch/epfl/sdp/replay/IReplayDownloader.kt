package ch.epfl.sdp.replay

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.UnitCallback
import java.io.File

interface IReplayDownloader {
    interface IReplayDownload {
        fun cancel()
    }

    fun download(gameID: String,
                 file: File,
                 successCallback: UnitCallback,
                 failureCallback: Callback<String>): IReplayDownload
}