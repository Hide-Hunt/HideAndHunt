package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.User

object MockGameLobbyRepository : IGameLobbyRepository {
    private const val gameId = 1
    private const val gameName = "My mock game"
    private const val gameDuration = 1200
    private val players = listOf(
            Participation(User("George Kittle", 85), false, "CAFE", PlayerParametersFragment.Faction.PREY),
            Participation(User("Nick Bosa", 97), false, "0A0A", PlayerParametersFragment.Faction.PREDATOR),
            Participation(User("Richard Sherman", 25), false, "C0BA", PlayerParametersFragment.Faction.PREDATOR))



    override fun getGameId(cb: (Int) -> Unit) {
        cb(gameId)
    }

    override fun getGameName(cb: (String) -> Unit) {
        cb(gameName)
    }

    override fun getGameDuration(cb: (Int) -> Unit) {
        cb(gameDuration)
    }

    override fun getPlayers(cb: (List<Participation>) -> Unit) {
        cb(players)
    }

    override fun getAdminId(cb: (Int) -> Unit) {
        cb(players[1].user.uid)
    }

    override fun changePlayerReady(user : User) {
        for (player in players) {
            if (player.user.uid == user.uid) player.ready = !player.ready
        }
    }

}