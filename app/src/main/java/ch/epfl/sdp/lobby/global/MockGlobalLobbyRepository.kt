package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.user.User
import java.util.*

class MockGlobalLobbyRepository : IGlobalLobbyRepository {

    private val participation1 = listOf<Participation>(Participation("JeanMichel", false, "FFCA", 42, PlayerFaction.PREY, 1))
    private val participation2 = listOf<Participation>(
            Participation("George Kittle", true, "CAFE", 85, PlayerFaction.PREY, 0),
            Participation("Nick Bosa", true, "0A0A", 97, PlayerFaction.PREDATOR, 0),
            Participation("Richard Sherman", true, "C0BA", 25, PlayerFaction.PREDATOR, 0)
    )
    private var games = listOf<Game>(
            Game(1, "Classical Game in Geneva", "Alex",
                    3600, emptyMap(),
                    GameState.LOBBY, emptyList(),
                    Date(3243432), Date(3243213432),
                    Date(32434332113212), 0),
            Game(2, "Classical Game in Lausanne", "Maxime",
                    7200, emptyMap(),
                    GameState.ENDED, participation1,
                    Date(3243432), Date(3243213432),
                    Date(32434332113212), 0),
            Game(3, "Battle Royale in San Francisco", "JimmyG",
                    3600, emptyMap(),
                    GameState.STARTED, participation2,
                    Date(3243432), Date(3243213432),
                    Date(32434332113212), 0)
    )


    override fun getAllGames(cb: Callback<List<Game>>)= cb(games)
}