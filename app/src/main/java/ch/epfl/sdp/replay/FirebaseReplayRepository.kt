package ch.epfl.sdp.replay

import android.content.Context
import ch.epfl.sdp.db.AppDatabase
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.FirebaseConstants.GAME_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.USER_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.USER_GAME_HISTORY_COLLECTION
import ch.epfl.sdp.game.data.Game
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

@InternalCoroutinesApi
class FirebaseReplayRepository(val context: Context) : IReplayRepository {
    private val fs: FirebaseFirestore = Firebase.firestore
    private val localReplayStore = LocalReplayStore(context)

    private fun getAllGamesLocally(cb: Callback<List<ReplayInfo>>) {
        GlobalScope.launch {
            AppDatabase.instance(context).replayDao().getAll().let {
                it.forEach { replayInfo ->
                    replayInfo.localCopy = localReplayStore.getFile(replayInfo.gameID).exists()
                }
                withContext(Dispatchers.Main) {
                    cb(it)
                }
            }
        }
    }

    private fun getAllGamesOnline(userID: String, cb: Callback<List<ReplayInfo>>) {
        fs.collection(USER_COLLECTION).document(userID).get().addOnSuccessListener { user ->
            val gameHistory = user[USER_GAME_HISTORY_COLLECTION] as List<*>
            fs.collection(GAME_COLLECTION).whereIn("id", gameHistory).get().addOnSuccessListener { games ->
                val replays = games.map { it.toObject<Game>() }.map { game ->
                    val playerInfo = game.participation.first { it.userID == userID }
                    ReplayInfo(
                            game.id,
                            game.name,
                            game.creationDate.time,
                            game.endDate.time,
                            playerInfo.score,
                            playerInfo.faction,
                            localReplayStore.getFile(game.id).exists()
                    )
                }
                GlobalScope.launch {
                    val dao = AppDatabase.instance(context).replayDao()
                    replays.forEach { dao.insert(it) }
                }
                cb(replays)
            }
        }.addOnFailureListener {
            getAllGamesLocally(cb)
        }
    }

    override fun getAllGames(userID: String, cb: Callback<List<ReplayInfo>>) {
        if (userID == "") {
            getAllGamesLocally(cb)
        } else {
            getAllGamesOnline(userID, cb)
        }
    }
}