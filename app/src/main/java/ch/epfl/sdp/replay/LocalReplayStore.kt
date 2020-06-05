package ch.epfl.sdp.replay

import android.content.Context
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.db.AppDatabaseCompanion
import ch.epfl.sdp.db.Callback
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class LocalReplayStore(private val context: Context) {
    @Inject lateinit var myAppDatabase: AppDatabaseCompanion
    private val REPLAYDIR = File(context.cacheDir.absolutePath + "/replays")

    init {
        (context.applicationContext as HideAndHuntApplication).appComponent.inject(this)
    }

    fun createReplayDir() {
        if(!REPLAYDIR.exists()) {
            REPLAYDIR.mkdirs()
        }
    }

    fun getTmpFile(id: String) = File(REPLAYDIR, "game_$id.tmp")
    fun getFile(id: String) = File(REPLAYDIR, "$id.game")

    fun getList(cb: Callback<List<ReplayInfo>>) {
        GlobalScope.launch {
            myAppDatabase.instance(context).replayDao().getAll().let {
                it.forEach { replayInfo ->
                    replayInfo.localCopy = getFile(replayInfo.gameID).exists()
                }
                withContext(Dispatchers.Main) {
                    cb(it)
                }
            }
        }
    }

    fun saveList(replays: List<ReplayInfo>) {
        GlobalScope.launch {
            val dao = myAppDatabase.instance(context).replayDao()
            replays.forEach { dao.insert(it) }
        }
    }
}