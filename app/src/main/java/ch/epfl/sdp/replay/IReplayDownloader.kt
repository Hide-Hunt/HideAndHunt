package ch.epfl.sdp.replay

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.UnitCallback

interface IReplayDownloader {
    interface IReplayDownload {
        fun cancel()
    }
    fun download(gameID: Int, successCallback: UnitCallback, failureCallback: Callback<String>): IReplayDownload
}