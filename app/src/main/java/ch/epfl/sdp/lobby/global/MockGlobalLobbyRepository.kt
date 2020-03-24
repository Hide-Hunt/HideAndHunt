package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.User
import java.util.*

class MockGlobalLobbyRepository : IGlobalLobbyRepository {

    private val participation1 = listOf<Participation>(Participation(User("JeanMichel", 42), false, "FFCA", PlayerFaction.PREY))
    private val participation2 = listOf<Participation>(
            Participation(User("George Kittle", 85), true, "CAFE", PlayerFaction.PREY),
            Participation(User("Nick Bosa", 97), true, "0A0A", PlayerFaction.PREDATOR),
            Participation(User("Richard Sherman", 25), true, "C0BA", PlayerFaction.PREDATOR)
    )
    private var games = listOf<Game>(
            Game(1, "Classical Game in Geneva", "Alex",
                    3600, emptyMap(),
                    GameState.LOBBY, emptyList(),
                    Date(3243432), Date(3243213432),
                    Date(32434332113212)),
            Game(2, "Classical Game in Lausanne", "Maxime",
                    7200, emptyMap(),
                    GameState.ENDED, participation1,
                    Date(3243432), Date(3243213432),
                    Date(32434332113212)),
            Game(3, "Battle Royale in San Francisco", "JimmyG",
                    3600, emptyMap(),
                    GameState.STARTED, participation2,
                    Date(3243432), Date(3243213432),
                    Date(32434332113212))
    )


    override fun getAllGames(cb: Callback<List<Game>>)= cb(games)
}