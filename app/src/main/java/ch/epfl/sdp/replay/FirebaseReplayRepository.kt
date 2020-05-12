package ch.epfl.sdp.replay

import android.content.Context
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.FirebaseConstants.GAME_COLLECTION
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.user.IUserRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import javax.inject.Inject

class FirebaseReplayRepository(val context: Context) : IReplayRepository {
    private val fs: FirebaseFirestore = Firebase.firestore
    private val localReplayStore = LocalReplayStore(context)

    @Inject
    lateinit var userRepo: IUserRepo
    init {
        (context.applicationContext as HideAndHuntApplication).appComponent.inject(this)
    }

    private fun getAllGamesOnline(userID: String, cb: Callback<List<ReplayInfo>>) {
        userRepo.getGameHistory(userID) { gameHistory ->
            fs.collection(GAME_COLLECTION).whereIn("id", gameHistory).get().addOnSuccessListener { games ->
                games.map { it.toObject<Game>() }
                        .map { ReplayInfo.fromGame(userID, it, localReplayStore) }
                        .let { replays ->
                            localReplayStore.saveList(replays)
                            cb(replays)
                        }
            }.addOnFailureListener {
                cb(emptyList())
            }
        }
    }

    override fun getAllGames(userID: String, cb: Callback<List<ReplayInfo>>) {
        if (userID.isEmpty()) {
            localReplayStore.getList(cb)
        } else {
            getAllGamesOnline(userID, cb)
        }
    }
}