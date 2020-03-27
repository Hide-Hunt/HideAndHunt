package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import ch.epfl.sdp.user.User
import java.sql.Time

object FakeMockGameLobbyRepo : IGameLobbyRepository {
    private const val gameId = 1
    private const val gameName = "My mock game"
    private const val gameDuration = 1200
    private val players = mutableListOf(
            Participation(User("George Kittle", 85), false, "CAFE", PlayerFaction.PREDATOR),
            Participation(User("Nick Bosa", 97), false, "0A0A", PlayerFaction.PREDATOR),
            Participation(User("Richard Sherman", 25), false, "C0BA", PlayerFaction.PREDATOR),
            Participation(User("Dummy User", 23), false, "AB00", PlayerFaction.PREY))

    override fun createGame(gameName: String, gameDuration: Time): Int {
        return 42
    }


    override fun getGameId(cb : Callback<Int>) {
        cb(gameId)
    }

    override fun getGameName(cb : Callback<String>) {
        cb(gameName)
    }

    override fun getGameDuration(cb : Callback<Int>) {
        cb(gameDuration)
    }

    override fun getParticipations(cb : Callback<List<Participation>>) {
        assert(false)
        cb(players)
    }

    override fun getPlayers(cb : Callback<List<Player>>) {
        var mPlayers: List<Player> = ArrayList()
        for(p in players) {
            mPlayers = if(p.faction == PlayerFaction.PREY) mPlayers + Prey(p.user.uid, p.tag) else mPlayers + Predator(p.user.uid)
        }
        cb(mPlayers)
    }

    override fun getAdminId(cb : Callback<Int>) {
        cb(players[1].user.uid)
    }

    override fun changePlayerReady(uid : Int) {
        for (player in players) {
            if (player.user.uid == uid) player.ready = !player.ready
        }
    }

    override fun  setPlayerFaction(uid : Int, faction : PlayerFaction) {
        players.forEach { participation ->
            if(uid == participation.user.uid) participation.faction = faction }
    }

}