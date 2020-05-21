package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.FirebaseConstants.GAME_ACTION_QUEUE_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.GAME_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.GAME_PARTICIPATION_COLLECTION
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

    override fun addLocalParticipation(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback) {
        fs.collection(GAME_COLLECTION).document(gameId)
                .update(GAME_PARTICIPATION_COLLECTION, FieldValue.arrayUnion(
                        Participation(LocalUser.uid, Faction.PREDATOR, false, "", "")))
                .addOnSuccessListener { successCallback() }
                .addOnFailureListener { failureCallback() }
    }

    override fun createGame(gameName: String, gameDuration: Long): String {
        val newGameRef = fs.collection(GAME_COLLECTION).document() //TODO: Better id generation
        newGameRef.set(Game(
                newGameRef.id,
                gameName,
                LocalUser.uid,
                gameDuration,
                emptyMap(), //TODO: For now no params
                listOf(Participation(LocalUser.uid, Faction.PREDATOR, false, "", "")), //TODO: Add local user participation
                Date(),
                Date((Int.MAX_VALUE / 2).toLong()), //TODO: For now the game is available to the max
                Date(),
                GameState.LOBBY
        ))
        return newGameRef.id
    }

    override fun getGameName(gameId: String, successCallback: Callback<String>, failureCallback: UnitCallback) {
        getGame(gameId, { successCallback(it.name) }, failureCallback)
    }

    override fun getGameDuration(gameId: String, successCallback: Callback<Long>, failureCallback: UnitCallback) {
        getGame(gameId, { successCallback(it.duration) }, failureCallback)
    }

    override fun getPlayers(gameId: String, successCallback: Callback<List<Player>>, failureCallback: UnitCallback) {
        getParticipations(gameId, { list ->
            successCallback(list.sortedBy { it.userID }.withIndex().map { x -> x.value.toPlayer(x.index) })
        }, failureCallback)
    }

    override fun getParticipations(gameId: String, successCallback: Callback<List<Participation>>, failureCallback: UnitCallback) {
        getGame(gameId, { successCallback(it.participation) }, failureCallback)
    }

    override fun getAdminId(gameId: String, successCallback: Callback<String>, failureCallback: UnitCallback) {
        getGame(gameId, { successCallback(it.adminID) }, failureCallback)
    }

    override fun requestGameLaunch(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback) {
        getGame(gameId, { game ->
            if (game.participation.all { p -> p.ready }) {
                fs.collection(GAME_ACTION_QUEUE_COLLECTION).add(hashMapOf(
                        "timestamp" to FieldValue.serverTimestamp(),
                        "action" to "start_game",
                        "game_id" to gameId))
                        .addOnSuccessListener { successCallback() }
                        .addOnFailureListener { failureCallback() }
            } else { failureCallback() }
        }, failureCallback)
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
                                        successCallback: UnitCallback, failureCallback: UnitCallback) {
        getParticipations(gameId, { participation ->
            val myParticipationIndex = participation.indexOfFirst { act_participation -> act_participation.userID == uid }
            modifier(participation[myParticipationIndex])
            fs.collection(GAME_COLLECTION).document(gameId)
                    .update(GAME_PARTICIPATION_COLLECTION, participation)
                    .addOnSuccessListener { successCallback() }
                    .addOnFailureListener { failureCallback() }
        }, failureCallback)
    }

    override fun setPlayerReady(gameId: String, uid: String, ready: Boolean,
                                successCallback: UnitCallback, failureCallback: UnitCallback) {
        updateUserParticipation(gameId, uid, {it.ready = ready}, successCallback, failureCallback)
    }

    override fun setPlayerFaction(gameId: String, uid: String, faction: Faction,
                                  successCallback: UnitCallback, failureCallback: UnitCallback) {
        updateUserParticipation(gameId, uid, {it.faction = faction}, successCallback, failureCallback)
    }

    override fun setPlayerTag(gameId: String, uid: String, tag: String,
                              successCallback: UnitCallback, failureCallback: UnitCallback) {
        updateUserParticipation(gameId, uid, {it.tag = tag}, successCallback, failureCallback)
    }

    override fun removeLocalParticipation(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback) {
        getParticipations(gameId, { participation ->
            val myParticipation = participation.first { act_participation -> act_participation.userID == LocalUser.uid }
            fs.collection(GAME_COLLECTION).document(gameId)
                    .update(GAME_PARTICIPATION_COLLECTION, FieldValue.arrayRemove(myParticipation))
                    .addOnSuccessListener { successCallback() }
                    .addOnFailureListener { failureCallback() }
        }, failureCallback)
    }
}