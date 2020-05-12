package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player

object MockGameLobbyRepository : IGameLobbyRepository {
    val players = mutableListOf(
            Participation("u53r1d", Faction.PREDATOR, false, "u53rt4g", ""),
            Participation("George Kittle", Faction.PREDATOR, false, "CAFE", ""),
            Participation("Nick Bosa", Faction.PREDATOR, false, "0A0A", ""),
            Participation("Richard Sherman", Faction.PREDATOR, false, "C0BA", ""),
            Participation("Dummy ", Faction.PREDATOR, false, "AB00", ""),
            Participation("Hello World", Faction.PREY, true, "C0B0", ""),
            Participation("Morgan Freeman", Faction.PREDATOR, false, "0BBB", ""),
            Participation("Jack Sparrow", Faction.PREY, true, "0AAC", ""),
            Participation("Britney Spears", Faction.PREDATOR, false, "AC00", ""),
            Participation("Spiderman", Faction.PREDATOR, false, "A0AA", ""),
            Participation("Neymar Jr", Faction.PREDATOR, false, "C000", "")
    )

    override fun addLocalParticipation(gameId: String) = Unit //No code

    override fun removeLocalParticipation(gameId: String) = Unit //No code

    override fun createGame(gameName: String, gameDuration: Long): String = "42"

    override fun getGameName(gameId: String, cb: Callback<String>) = cb("My mock game")

    override fun getGameDuration(gameId: String, cb: Callback<Long>) = cb(1200L)

    override fun getParticipations(gameId: String, cb: Callback<List<Participation>>) = cb(players)

    override fun getPlayers(gameId: String, cb: Callback<List<Player>>) {
        cb(players.sortedBy { it.userID }.withIndex().map { p -> p.value.toPlayer(p.index) })
    }

    override fun getAdminId(gameId: String, cb: Callback<String>) { cb(players[1].userID) }

    override fun changePlayerReady(gameId: String, uid: String) {
        players.first { it.userID == uid }.let { it.ready = !it.ready }
    }

    override fun setPlayerReady(gameId: String, uid: String, ready: Boolean) {
        players.first { it.userID == uid }.ready = ready
    }

    override fun setPlayerFaction(gameId: String, uid: String, faction: Faction) {
        players.first { it.userID == uid }.faction = faction
    }

    override fun setPlayerTag(gameId: String, uid: String, tag: String) {
        players.first { it.userID == uid }.tag = tag
    }
}