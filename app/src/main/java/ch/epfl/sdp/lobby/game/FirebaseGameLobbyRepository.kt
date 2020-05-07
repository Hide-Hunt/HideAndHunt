package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.IDHelper
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.abs

/**
 * Repository for Firestore databse interactions
 */
class FirebaseGameLobbyRepository : IGameLobbyRepository {

    private var fs: FirebaseFirestore = Firebase.firestore

    /**
     * Get the game associated to a given id
     */
    private fun getFirebaseGameWithID(gameID: Int, cb: Callback<String>) {
        fs.collection("games").get().addOnSuccessListener { result ->
            val match = result.filter { x -> (x["id"] as Long).toInt() == gameID }
            if(match.isEmpty()) {
                //no game matches the given id
                cb("")
            } else {
                //found a game matching the id
                cb(match[0].id)
            }
        }
    }

    override fun addLocalParticipation(gameId: Int) {
        fs.collection("participations").add(
                Participation(
                        LocalUser.username,
                        true,
                        "",
                        IDHelper.getPlayerID(),
                        PlayerFaction.PREDATOR,
                        gameId
                )
        )
    }

    override fun createGame(gameName: String, gameDuration: Long): Int {
        val gameID = abs(gameName.hashCode()) //TODO: Better id generation
        fs.collection("games").add(
                Game(
                        gameID,
                        gameName,
                        LocalUser.username,
                        gameDuration,
                        emptyMap(), //TODO: For now no params
                        GameState.LOBBY,
                        emptyList(), //TODO: Add local user participation
                        Date(),
                        Date((Int.MAX_VALUE / 2).toLong()), //TODO: For now the game is available to the max
                        Date(),
                        IDHelper.getPlayerID()
                )
        )
        return gameID
    }

    override fun getGameName(gameId: Int, cb: Callback<String>) {
        getFirebaseGameWithID(gameId) { fid ->
            fs.collection("games").document(fid).get().addOnSuccessListener {
                cb(it["name"] as String)
            }
        }
    }

    override fun getGameDuration(gameId: Int, cb: Callback<Long>) {
        getFirebaseGameWithID(gameId) { fid ->
            fs.collection("games").document(fid).get().addOnSuccessListener {
                cb(it["duration"] as Long)
            }
        }
    }

    override fun getPlayers(gameId: Int, cb: Callback<List<Player>>) {
        getParticipations(gameId) {
            cb(it.map { x ->
                //callback to the right instance of Player
                if(x.faction == PlayerFaction.PREY)
                    Prey(x.playerID, x.tag)
                else
                    Predator(x.playerID)
            })
        }
    }

    override fun getParticipations(gameId: Int, cb: Callback<List<Participation>>) {
        val players: MutableList<Participation> = ArrayList()
        fs.collection("participations").whereEqualTo("gameID", gameId).get().addOnSuccessListener { documents ->

            for(doc in documents) {
                if(players.none { p -> p.playerID == (doc["playerID"] as Long).toInt() }) {
                    players.add(Participation(
                            doc["username"] as String,
                            doc["ready"] as Boolean,
                            doc["tag"] as String,
                            (doc["playerID"] as Long).toInt(),
                            PlayerFaction.valueOf(doc["faction"] as String),
                            (doc["gameID"] as Long).toInt()
                    ))
                }
            }
            cb(players)
        }
    }

    override fun getAdminId(gameId: Int, cb: Callback<Int>) {
        getFirebaseGameWithID(gameId) { fid ->
            fs.collection("games").document(fid).get().addOnSuccessListener {
                cb((it["adminID"] as Long).toInt())
            }
        }
    }

    override fun changePlayerReady(gameId: Int, uid: Int) {
        setPlayerReady(gameId, uid, true)
    }

    override fun setPlayerReady(gameId: Int, uid: Int, ready: Boolean) {
        fs.collection("participations").whereEqualTo("playerID", uid).get().addOnSuccessListener { documents ->
            for(doc in documents) {
                fs.collection("participations").document(doc.id).update("ready", ready)
            }
        }
    }

    override fun setPlayerFaction(gameId: Int, uid: Int, faction: PlayerFaction) {
        fs.collection("participations").whereEqualTo("playerID", uid).get().addOnSuccessListener { documents ->
            for(doc in documents) {
                fs.collection("participations").document(doc.id).update("faction", faction)
            }
        }
    }

    override fun setPlayerTag(gameId: Int, uid: Int, tag: String) {
        fs.collection("participations").whereEqualTo("playerID", uid).get().addOnSuccessListener { documents ->
            for(doc in documents) {
                fs.collection("participations").document(doc.id).update("tag", tag)
            }
        }
    }

    override fun removeLocalParticipation(gameId: Int) {
        fs.collection("participations").whereEqualTo("gameID", gameId).get().addOnSuccessListener {
            it.forEach { x -> x.reference.delete() }
        }
    }
}