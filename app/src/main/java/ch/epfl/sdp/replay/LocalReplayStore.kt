package ch.epfl.sdp.replay

import android.content.Context
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.db.AppDatabase
import ch.epfl.sdp.db.AppDatabaseCompanion
import ch.epfl.sdp.db.Callback
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class LocalReplayStore(private val context: Context) {
    @Inject lateinit var myAppDatabase: AppDatabaseCompanion

    init {
        (context.applicationContext as HideAndHuntApplication).appComponent.inject(this)
    }

    fun getPath(id: String): String = context.filesDir.absolutePath + "/replays/" + id

    fun getFile(id: String): File = File(getPath(id))

    @InternalCoroutinesApi
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

    @InternalCoroutinesApi
    fun saveList(replays: List<ReplayInfo>) {
        GlobalScope.launch {
            val dao = AppDatabase.instance(context).replayDao()
            replays.forEach { dao.insert(it) }
        }
    }
}