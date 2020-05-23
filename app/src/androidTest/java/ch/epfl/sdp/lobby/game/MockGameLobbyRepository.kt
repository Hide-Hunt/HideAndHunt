package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.SuccFailCallbacks.*
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player

// Suppress is made to allow mutable public fields as they are used in the tests to simulate the repo logic
@Suppress("MemberVisibilityCanBePrivate", "MayBeConstant")
class MockGameLobbyRepository : IGameLobbyRepository {
    val players = mutableListOf(
            Participation("George Kittle", Faction.PREDATOR, false, "CAFE", ""),
            Participation("Nick Bosa", Faction.PREDATOR, false, "0A0A", ""),
            Participation("Richard Sherman", Faction.PREDATOR, false, "C0BA", ""),
            Participation("u53r1d", Faction.PREDATOR, false, "u53rt4g", ""),
            Participation("Dummy ", Faction.PREDATOR, false, "AB00", ""),
            Participation("Hello World", Faction.PREY, true, "C0B0", ""),
            Participation("Morgan Freeman", Faction.PREDATOR, false, "0BBB", ""),
            Participation("Jack Sparrow", Faction.PREY, true, "0AAC", ""),
            Participation("Britney Spears", Faction.PREDATOR, false, "AC00", ""),
            Participation("Spiderman", Faction.PREDATOR, false, "A0AA", ""),
            Participation("Neymar Jr", Faction.PREDATOR, false, "C000", "")
    )
    var gameState = GameState.LOBBY
    val gameStartListeners = HashMap<String, IGameLobbyRepository.OnGameStartListener?>()

    override fun addLocalParticipation(gameId: String, cb: UnitSuccFailCallback) = Unit //No code

    override fun removeLocalParticipation(gameId: String, cb: UnitSuccFailCallback) = Unit //No code

    override fun createGame(gameName: String, gameDuration: Long): String = "42"

    override fun getGameName(gameId: String, cb: SuccFailCallback<String>) = cb.success("My mock game")

    override fun getGameDuration(gameId: String, cb: SuccFailCallback<Long>) = cb.success(1200L)

    override fun getParticipation(gameId: String, cb: SuccFailCallback<List<Participation>>) = cb.success(players)

    override fun getPlayers(gameId: String, cb: SuccFailCallback<List<Player>>) {
        cb.success(players.sortedBy { it.userID }.withIndex().map { p -> p.value.toPlayer(p.index) })
    }

    override fun getAdminId(gameId: String, cb: SuccFailCallback<String>) { cb.success(players[1].userID) }

    override fun requestGameLaunch(gameId: String, cb: UnitSuccFailCallback) {
        if (gameState == GameState.LOBBY) {
            cb.success()
            gameState = GameState.STARTED
            if (gameStartListeners.containsKey(gameId)) {
                gameStartListeners[gameId]?.onGameStart()
            }
        } else {
            cb.failure()
        }
    }

    override fun setOnGameStartListener(gameId: String, listener: IGameLobbyRepository.OnGameStartListener?) {
        gameStartListeners[gameId] = listener
    }

    override fun setPlayerReady(gameId: String, uid: String, ready: Boolean,
                                   cb: UnitSuccFailCallback) {
        players.first { it.userID == uid }.ready = ready
        cb.success()
    }

    override fun setPlayerFaction(gameId: String, uid: String, faction: Faction,
                                   cb: UnitSuccFailCallback) {
        players.first { it.userID == uid }.faction = faction
        cb.success()
    }

    override fun setPlayerTag(gameId: String, uid: String, tag: String,
                                   cb: UnitSuccFailCallback) {
        players.first { it.userID == uid }.tag = tag
        cb.success()
    }
}