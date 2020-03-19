package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.User

object MockGameLobbyRepository : IGameLobbyRepository {
    private var counter = 0
    private const val gameId = 1
    private const val gameName = "My mock game"
    private const val gameDuration = 1200
    private val players = mutableListOf(
            Participation(User("George Kittle", 85), false, "CAFE", PlayerParametersFragment.Faction.PREDATOR),
            Participation(User("Nick Bosa", 97), false, "0A0A", PlayerParametersFragment.Faction.PREDATOR),
            Participation(User("Richard Sherman", 25), false, "C0BA", PlayerParametersFragment.Faction.PREDATOR),
            Participation(User("Dummy User", 23), false, "AB00", PlayerParametersFragment.Faction.PREY),
            Participation(User("Hello World", 42), true, "C0B0", PlayerParametersFragment.Faction.PREY),
            Participation(User("Morgan Freeman", 91), false, "0BBB", PlayerParametersFragment.Faction.PREDATOR),
            Participation(User("Jack Sparrow", 7), true, "0AAC", PlayerParametersFragment.Faction.PREY),
            Participation(User("Britney Spears", 24), false, "AC00", PlayerParametersFragment.Faction.PREDATOR),
            Participation(User("Spiderman", 25), false, "A0AA", PlayerParametersFragment.Faction.PREDATOR),
            Participation(User("Neymar Jr", 26), false, "C000", PlayerParametersFragment.Faction.PREDATOR))



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
        if (counter != 0) players.add(Participation(User("Player$counter",10 + counter),
                false,"0ABC",PlayerParametersFragment.Faction.PREY))
        ++counter
        cb(players)
    }

    override fun getAdminId(cb: (Int) -> Unit) {
        cb(players[1].user.uid)
    }

    override fun changePlayerReady(uid : Int) {
        for (player in players) {
            if (player.user.uid == uid) player.ready = !player.ready
        }
    }

    override fun setPlayerFaction(uid: Int, faction: PlayerParametersFragment.Faction) {
        players.forEach { participation ->
            if(uid == participation.user.uid) participation.faction = faction }
    }

}