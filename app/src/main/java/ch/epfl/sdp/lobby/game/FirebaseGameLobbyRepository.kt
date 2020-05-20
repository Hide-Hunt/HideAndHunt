package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.FirebaseConstants.GAME_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.GAME_PARTICIPATION_COLLECTION
import ch.epfl.sdp.db.UnitCallback
import ch.epfl.sdp.game.data.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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

    override fun addLocalParticipation(gameId: String) {
        fs.collection(GAME_COLLECTION).document(gameId)
                .update(GAME_PARTICIPATION_COLLECTION, FieldValue.arrayUnion(
                        Participation(LocalUser.uid, Faction.PREDATOR, false, "", "")))
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

    override fun getGameName(gameId: String, cb: Callback<String>) {
        fs.collection(GAME_COLLECTION).document(gameId).get()
                .addOnSuccessListener { cb(it["name"] as String) }
    }

    override fun getGameDuration(gameId: String, cb: Callback<Long>) {
        fs.collection(GAME_COLLECTION).document(gameId).get()
                .addOnSuccessListener { cb(it["duration"] as Long) }
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

    private fun setHelper(gameId: String, cb: UnitCallback, modifier: participationModifier){
        fs.collection(GAME_COLLECTION).document(gameId).get()
                .addOnSuccessListener { doc ->
                    val participation = doc.toObject<Game>()!!.participation
                    val myParticipationIndex = participation.indexOfFirst { act_participation -> act_participation.userID == LocalUser.uid }
                    modifier(participation[myParticipationIndex])
                    fs.collection(GAME_COLLECTION).document(gameId)
                            .update(GAME_PARTICIPATION_COLLECTION, participation)
                    cb()
                }
    }

    override fun setPlayerReady(gameId: String, uid: String, ready: Boolean, cb: UnitCallback) { setHelper(gameId, cb){it.ready = ready} }

    override fun setPlayerFaction(gameId: String, uid: String, faction: Faction, cb: UnitCallback) { setHelper(gameId, cb){it.faction = faction} }

    override fun setPlayerTag(gameId: String, uid: String, tag: String, cb: UnitCallback) {setHelper(gameId, cb){it.tag = tag}}

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