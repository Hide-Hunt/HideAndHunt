package ch.epfl.sdp.replay

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.UnitCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FakeReplayDownloader() : IReplayDownloader {
    override fun download(gameID: Int, file: File, successCallback: UnitCallback, failureCallback: Callback<String>): IReplayDownloader.IReplayDownload {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                when (gameID) {
                    1 -> successCallback()
                    2 -> failureCallback("Wrong game ID")
                }
            }
        }

        return object : IReplayDownloader.IReplayDownload{
            override fun cancel() {failureCallback("Cancelled download for file $gameID")}
        }
    }

}
