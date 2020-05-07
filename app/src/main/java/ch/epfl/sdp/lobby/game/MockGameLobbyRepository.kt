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
    private const val gameDuration = 1200L
    private val players = mutableListOf(
            Participation("George Kittle", false, "CAFE", 85, PlayerFaction.PREDATOR, gameId),
            Participation("Nick Bosa", false, "0A0A", 97, PlayerFaction.PREDATOR, gameId),
            Participation("Richard Sherman", false, "C0BA", 25, PlayerFaction.PREDATOR, gameId),
            Participation("Dummy ", false, "AB00", 23, PlayerFaction.PREDATOR, gameId),
            Participation("Hello World", true, "C0B0", 42, PlayerFaction.PREY, gameId),
            Participation("Morgan Freeman", false, "0BBB", 1, PlayerFaction.PREDATOR, gameId),
            Participation("Jack Sparrow", true, "0AAC", 7, PlayerFaction.PREY, gameId),
            Participation("Britney Spears", false, "AC00", 24, PlayerFaction.PREDATOR, gameId),
            Participation("Spiderman", false, "A0AA", 25, PlayerFaction.PREDATOR, gameId),
            Participation("Neymar Jr", false, "C000", 26, PlayerFaction.PREDATOR, gameId)
    )

    override fun addLocalParticipation(gameId: Int) {
        //No code
    }

    override fun removeLocalParticipation(gameId: Int) {
        //No code
    }

    override fun createGame(gameName: String, gameDuration: Long): Int {
        return 42
    }

    override fun getGameName(gameId: Int, cb: Callback<String>) {
        cb(gameName)
    }

    override fun getGameDuration(gameId: Int, cb: Callback<Long>) {
        cb(gameDuration)
    }

    override fun getParticipations(gameId: Int, cb: Callback<List<Participation>>) {
        //add players to show refreshing works
        if (counter != 0) players.add(Participation("Player$counter",
                false, "0ABC", 10 + counter, PlayerFaction.PREY, gameId))
        ++counter
        cb(players)
    }

    override fun getPlayers(gameId: Int, cb: Callback<List<Player>>) {
        var mPlayers: List<Player> = ArrayList()
        for (p in players) {
            mPlayers = if (p.faction == PlayerFaction.PREY) mPlayers + Prey(p.playerID, p.tag) else mPlayers + Predator(p.playerID)
        }
        cb(mPlayers)
    }

    override fun getAdminId(gameId: Int, cb: Callback<Int>) {
        cb(players[1].playerID)
    }

    override fun changePlayerReady(gameId: Int, uid: Int) {
        for (player in players) {
            if (player.playerID == uid) {
                player.ready = !player.ready
                break
            }
        }
    }

    override fun setPlayerReady(gameId: Int, uid: Int, ready: Boolean) {
        for (player in players) {
            if (player.playerID == uid) {
                player.ready = ready
                break
            }
        }
    }

    override fun setPlayerFaction(gameId: Int, uid: Int, faction: PlayerFaction) {
        players.forEach { participation ->
            if (uid == participation.playerID) participation.faction = faction
        }
    }

    override fun setPlayerTag(gameId: Int, uid: Int, tag: String) {
        players.forEach { player ->
            if(uid == player.playerID) {
                player.tag = tag
                return
            }
        }
    }

}