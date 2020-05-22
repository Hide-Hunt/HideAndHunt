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

    private fun <T> getField(gameId: String, fieldName: String, cb: Callback<T>) {
        fs.collection(GAME_COLLECTION).document(gameId).get().addOnSuccessListener {
            @Suppress("UNCHECKED_CAST")
            cb(it[fieldName] as T)
        }
    }

    override fun addLocalParticipation(gameId: String) {
        fs.collection(GAME_COLLECTION).document(gameId)
                .update(GAME_PARTICIPATION_COLLECTION, FieldValue.arrayUnion(
                        Participation(LocalUser.uid, Faction.PREDATOR, false, "", "")))
    }

    override fun createGame(gameName: String, gameDuration: Long): String {
        val newGameRef = fs.collection(GAME_COLLECTION).document()
        newGameRef.set(Game(
                newGameRef.id,
                gameName,
                LocalUser.uid,
                gameDuration,
                emptyMap(), //TODO: For now no params
                listOf(Participation(LocalUser.uid, Faction.PREDATOR, false, "", "")),
                Date(),
                Date((Int.MAX_VALUE / 2).toLong()), //TODO: For now the game is available to the max
                Date(),
                GameState.LOBBY
        ))
        return newGameRef.id
    }

    override fun getGameName(gameId: String, cb: Callback<String>) {
        getField(gameId, "name", cb)
    }

    override fun getGameDuration(gameId: String, cb: Callback<Long>) {
        getField(gameId, "duration", cb)
    }

    override fun getPlayers(gameId: String, cb: Callback<List<Player>>) {
        getParticipations(gameId) { list ->
            cb(list.sortedBy { it.userID }.withIndex().map { x -> x.value.toPlayer(x.index) })
        }
    }

    override fun getParticipations(gameId: String, cb: Callback<List<Participation>>) {
        fs.collection(GAME_COLLECTION).document(gameId).get()
            .addOnSuccessListener { doc ->
                cb(doc.toObject<Game>()!!.participation)
            }
    }

    override fun getAdminId(gameId: String, cb: Callback<String>) {
        fs.collection(GAME_COLLECTION).document(gameId).get()
                .addOnSuccessListener { cb(it.toObject<Game>()!!.adminID) }
    }

    override fun changePlayerReady(gameId: String, uid: String, cb: UnitCallback) {
        setPlayerReady(gameId, uid, true, cb)
    }

    override fun requestGameLaunch(gameId: String) {
        fs.collection(GAME_ACTION_QUEUE_COLLECTION).add(hashMapOf(
                "timestamp" to FieldValue.serverTimestamp(),
                "action" to "start_game",
                "game_id" to gameId
        ))
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

    private fun updateUserParticipation(gameId: String, uid: String, cb: UnitCallback, modifier: participationModifier){
        fs.collection(GAME_COLLECTION).document(gameId).get()
                .addOnSuccessListener { doc ->
                    val participation = doc.toObject<Game>()!!.participation
                    val myParticipationIndex = participation.indexOfFirst { act_participation -> act_participation.userID == uid }
                    modifier(participation[myParticipationIndex])
                    fs.collection(GAME_COLLECTION).document(gameId)
                            .update(GAME_PARTICIPATION_COLLECTION, participation)
                    cb()
                }
    }

    override fun setPlayerReady(gameId: String, uid: String, ready: Boolean, cb: UnitCallback) { updateUserParticipation(gameId, uid, cb){it.ready = ready} }

    override fun setPlayerFaction(gameId: String, uid: String, faction: Faction, cb: UnitCallback) { updateUserParticipation(gameId, uid, cb){it.faction = faction} }

    override fun setPlayerTag(gameId: String, uid: String, tag: String, cb: UnitCallback) {updateUserParticipation(gameId, uid, cb){it.tag = tag}}

    override fun removeLocalParticipation(gameId: String) {
        fs.collection(GAME_COLLECTION).document(gameId).get()
                .addOnSuccessListener { doc ->
                    val participation = doc.toObject<Game>()!!.participation
                    val myParticipation = participation.first { act_participation -> act_participation.userID == LocalUser.uid }
                    fs.collection(GAME_COLLECTION).document(gameId)
                            .update(GAME_PARTICIPATION_COLLECTION, FieldValue.arrayRemove(
                                    myParticipation))
                }
    }
}