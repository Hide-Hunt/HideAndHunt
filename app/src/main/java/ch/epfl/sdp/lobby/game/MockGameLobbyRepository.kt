package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import ch.epfl.sdp.user.User
import java.sql.Time

object MockGameLobbyRepository : IGameLobbyRepository {
    private var counter = 0
    private const val gameId = 1
    private const val gameName = "My mock game"
    private const val gameDuration = 1200
    private val players = mutableListOf(
            Participation(User("George Kittle", "85"), false, "CAFE", PlayerFaction.PREDATOR),
            Participation(User("Nick Bosa", "97"), false, "0A0A", PlayerFaction.PREDATOR),
            Participation(User("Richard Sherman", "25"), false, "C0BA", PlayerFaction.PREDATOR),
            Participation(User("Dummy User", "23"), false, "AB00", PlayerFaction.PREDATOR),
            Participation(User("Hello World", "42"), true, "C0B0", PlayerFaction.PREY),
            Participation(User("Morgan Freeman", "1"), false, "0BBB", PlayerFaction.PREDATOR),
            Participation(User("Jack Sparrow", "7"), true, "0AAC", PlayerFaction.PREY),
            Participation(User("Britney Spears", "24"), false, "AC00", PlayerFaction.PREDATOR),
            Participation(User("Spiderman", "25"), false, "A0AA", PlayerFaction.PREDATOR),
            Participation(User("Neymar Jr", "26"), false, "C000", PlayerFaction.PREDATOR))

    override fun createGame(gameName: String, gameDuration: Time): Int {
        return 42
    }

    override fun getGameId(cb: Callback<Int>) {
        cb(gameId)
    }

    override fun getGameName(cb: Callback<String>) {
        cb(gameName)
    }

    override fun getGameDuration(cb: Callback<Int>) {
        cb(gameDuration)
    }

    override fun getParticipations(cb: Callback<List<Participation>>) {
        //add players to show refreshing works
        if (counter != 0) players.add(Participation(User("Player$counter", (10 + counter).toString() ),
                false, "0ABC", PlayerFaction.PREY))
        ++counter
        cb(players)
    }

    override fun getPlayers(cb: Callback<List<Player>>) {
        var mPlayers: List<Player> = ArrayList()
        for (p in players) {
            mPlayers = if (p.faction == PlayerFaction.PREY) mPlayers + Prey(p.user.uid, p.tag) else mPlayers + Predator(p.user.uid)
        }
        cb(mPlayers)
    }

    override fun getAdminId(cb: Callback<String>) {
        cb(players[1].user.uid)
    }

    override fun changePlayerReady(uid: String) {
        for (player in players) {
            if (player.user.uid == uid) {
                player.ready = !player.ready
                break
            }
        }
    }

    override fun setPlayerReady(uid: String, ready: Boolean) {
        for (player in players) {
            if (player.user.uid == uid) {
                player.ready = ready
                break
            }
        }
    }

    override fun setPlayerFaction(uid: String, faction: PlayerFaction) {
        players.forEach { participation ->
            if (uid == participation.user.uid) participation.faction = faction
        }
    }

    override fun setPlayerTag(uid: String, tag: String) {
        players.forEach { player ->
            if(uid == player.user.uid) {
                player.tag = tag
                return
            }
        }
    }

    override fun addPlayer(uid: String, username: String) : String{
        players.add(Participation(User(username, uid), false, "", PlayerFaction.PREDATOR))
        return uid
    }

    override fun removePlayer(uid: String) {
        val indexToRemove : Int
        players.forEach{ participation ->
            if(participation.user.uid == uid){
                players.remove(participation)
                return
            }
        }
    }

    override fun createPlayer(username: String): String {
        val uid : String = "0"
        players.add(Participation(User(username, uid), false, "", PlayerFaction.PREDATOR))
        return uid
    }

}