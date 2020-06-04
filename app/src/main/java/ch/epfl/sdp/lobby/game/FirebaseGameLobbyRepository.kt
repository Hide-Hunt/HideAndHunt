package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.FirebaseConstants.GAME_ACTION_QUEUE_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.GAME_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.GAME_PARTICIPATION_COLLECTION
import ch.epfl.sdp.db.SuccFailCallbacks.*
import ch.epfl.sdp.db.UnitCallback
import ch.epfl.sdp.game.data.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

typealias participationModifier = (Participation) -> Unit

/**
 * Repository for Firestore database interactions with the game lobby
 */
class FirebaseGameLobbyRepository : IGameLobbyRepository {
    private var fs: FirebaseFirestore = Firebase.firestore
    private var gameStartListener: IGameLobbyRepository.OnGameStartListener? = null
    private var gameStartSnapshotRegistration: ListenerRegistration? = null

    private fun getGame(gameId: String, successCallback: Callback<Game>, failureCallback: UnitCallback) {
        fs.collection(GAME_COLLECTION).document(gameId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        doc.toObject<Game>().let {
                            if (it == null) failureCallback()
                            else successCallback(doc.toObject<Game>()!!)
                        }
                    } else { failureCallback() }
                }
                .addOnFailureListener { failureCallback() }
    }

    override fun addLocalParticipation(gameId: String, cb: UnitSuccFailCallback) {
        fs.collection(GAME_COLLECTION).document(gameId)
                .update(GAME_PARTICIPATION_COLLECTION, FieldValue.arrayUnion(
                        Participation(LocalUser.uid, Faction.PREDATOR, true, "", "")))
                .addOnSuccessListener { cb.success() }
                .addOnFailureListener { cb.failure() }
    }

    override fun createGame(gameName: String, gameDuration: Long): String {
        val newGameRef = fs.collection(GAME_COLLECTION).document() //TODO: Better id generation
        newGameRef.set(Game(
                newGameRef.id,
                gameName,
                LocalUser.uid,
                gameDuration,
                emptyMap(), //TODO: For now no params
                listOf(Participation(LocalUser.uid, Faction.PREDATOR, true, "", "")), //TODO: Add local user participation
                Date(),
                Date((Int.MAX_VALUE / 2).toLong()), //TODO: For now the game is available to the max
                Date(),
                GameState.LOBBY
        ))
        return newGameRef.id
    }

    override fun getGameName(gameId: String, cb: SuccFailCallback<String>) {
        getGame(gameId, { cb.success(it.name) }, cb.failure)
    }

    override fun getGameDuration(gameId: String, cb: SuccFailCallback<Long>) {
        getGame(gameId, { cb.success(it.duration) }, cb.failure)
    }

    override fun getPlayers(gameId: String, cb: SuccFailCallback<List<Player>>) {
        getParticipation(gameId, SuccFailCallback({ list ->
            cb.success(list.sortedBy { it.userID.capitalize() }.withIndex().map { x -> x.value.toPlayer(x.index) })
        }, cb.failure))
    }

    override fun getParticipation(gameId: String, cb: SuccFailCallback<List<Participation>>) {
        getGame(gameId, { cb.success(it.participation) }, cb.failure)
    }

    override fun getAdminId(gameId: String, cb: SuccFailCallback<String>) {
        getGame(gameId, { cb.success(it.adminID) }, cb.failure)
    }

    override fun requestGameLaunch(gameId: String, cb: UnitSuccFailCallback) {
        getGame(gameId, { game ->
            if (game.participation.all { p -> p.ready }) {
                fs.collection(GAME_ACTION_QUEUE_COLLECTION).add(hashMapOf(
                        "timestamp" to FieldValue.serverTimestamp(),
                        "action" to "start_game",
                        "game_id" to gameId))
                        .addOnSuccessListener { cb.success() }
                        .addOnFailureListener { cb.failure() }
            } else { cb.failure() }
        }, cb.failure)
    }

    override fun setOnGameStartListener(gameId: String, listener: IGameLobbyRepository.OnGameStartListener?) {
        gameStartSnapshotRegistration?.remove()
        gameStartSnapshotRegistration = null
        gameStartListener = listener

        if (gameStartListener == null) {
            return
        }

        val gameRef = fs.collection(GAME_COLLECTION).document(gameId)
        gameStartSnapshotRegistration = gameRef.addSnapshotListener { snapshot, e ->
            if (e != null) return@addSnapshotListener

            if (snapshot != null && snapshot.exists() && snapshot.toObject<Game>()!!.state == GameState.STARTED) {
                gameStartListener?.onGameStart()
            }
        }
    }

    private fun updateUserParticipation(gameId: String, uid: String, modifier: participationModifier,
                                        cb: UnitSuccFailCallback) {
        getParticipation(gameId, SuccFailCallback({ participation ->
            val myParticipationIndex = participation.indexOfFirst { act_participation -> act_participation.userID == uid }
            modifier(participation[myParticipationIndex])
            fs.collection(GAME_COLLECTION).document(gameId)
                    .update(GAME_PARTICIPATION_COLLECTION, participation)
                    .addOnSuccessListener { cb.success() }
                    .addOnFailureListener { cb.failure() }
        }, cb.failure))
    }

    override fun setPlayerReady(gameId: String, uid: String, ready: Boolean,
                                cb: UnitSuccFailCallback) {
        updateUserParticipation(gameId, uid, {it.ready = ready}, cb)
    }

    override fun setPlayerFaction(gameId: String, uid: String, faction: Faction,
                                  cb: UnitSuccFailCallback) {
        updateUserParticipation(gameId, uid, {it.faction = faction}, cb)
    }

    override fun setPlayerTag(gameId: String, uid: String, tag: String,
                              cb: UnitSuccFailCallback) {
        updateUserParticipation(gameId, uid, {it.tag = tag}, cb)
    }

    override fun removeLocalParticipation(gameId: String, cb: UnitSuccFailCallback) {
        getParticipation(gameId, SuccFailCallback({ participation ->
            val myParticipation = participation.first { act_participation -> act_participation.userID == LocalUser.uid }
            fs.collection(GAME_COLLECTION).document(gameId)
                    .update(GAME_PARTICIPATION_COLLECTION, FieldValue.arrayRemove(myParticipation))
                    .addOnSuccessListener { cb.success() }
                    .addOnFailureListener { cb.failure() }
        }, cb.failure))
    }
}