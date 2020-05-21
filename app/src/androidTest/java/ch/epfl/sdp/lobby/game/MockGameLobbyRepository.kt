package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.UnitCallback
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.GameState
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
    var gameState = GameState.LOBBY
    val gameStartListeners = HashMap<String, IGameLobbyRepository.OnGameStartListener?>()

    override fun addLocalParticipation(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback) = Unit //No code

    override fun removeLocalParticipation(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback) = Unit //No code

    override fun createGame(gameName: String, gameDuration: Long): String = "42"

    override fun getGameName(gameId: String, successCallback: Callback<String>, failureCallback: UnitCallback) = successCallback("My mock game")

    override fun getGameDuration(gameId: String, successCallback: Callback<Long>, failureCallback: UnitCallback) = successCallback(1200L)

    override fun getParticipations(gameId: String, successCallback: Callback<List<Participation>>, failureCallback: UnitCallback) = successCallback(players)

    override fun getPlayers(gameId: String, successCallback: Callback<List<Player>>, failureCallback: UnitCallback) {
        successCallback(players.sortedBy { it.userID }.withIndex().map { p -> p.value.toPlayer(p.index) })
    }

    override fun getAdminId(gameId: String, successCallback: Callback<String>, failureCallback: UnitCallback) { successCallback(players[1].userID) }

    override fun requestGameLaunch(gameId: String, successCallback: UnitCallback, failureCallback: UnitCallback) {
        if (gameState == GameState.LOBBY) {
            successCallback()
            gameState = GameState.STARTED
            if (gameStartListeners.containsKey(gameId)) {
                gameStartListeners[gameId]?.onGameStart()
            }
        } else {
            failureCallback()
        }
    }

    override fun setOnGameStartListener(gameId: String, listener: IGameLobbyRepository.OnGameStartListener?) {
        gameStartListeners[gameId] = listener
    }

    override fun setPlayerReady(gameId: String, uid: String, ready: Boolean,
                                   successCallback: UnitCallback, failureCallback: UnitCallback) {
        players.first { it.userID == uid }.ready = ready
        successCallback()
    }

    override fun setPlayerFaction(gameId: String, uid: String, faction: Faction,
                                   successCallback: UnitCallback, failureCallback: UnitCallback) {
        players.first { it.userID == uid }.faction = faction
        successCallback()
    }

    override fun setPlayerTag(gameId: String, uid: String, tag: String,
                                   successCallback: UnitCallback, failureCallback: UnitCallback) {
        players.first { it.userID == uid }.tag = tag
        successCallback()
    }
}