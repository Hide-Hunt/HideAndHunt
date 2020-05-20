package ch.epfl.sdp.lobby.global

import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.game.data.Participation
import java.util.*

class MockGlobalLobbyRepository : IGlobalLobbyRepository {

    private val participation1 = listOf(Participation("JeanMichel", Faction.PREY,false, "FFCA", ""))
    private val participation2 = listOf(
            Participation("George Kittle", Faction.PREY, true, "CAFE", ""),
            Participation("Nick Bosa", Faction.PREDATOR, true, "0A0A", ""),
            Participation("Richard Sherman", Faction.PREDATOR, true, "C0BA", "")
    )
    private var games = listOf(
            Game(
                    "1",
                    "Classical Game in Geneva",
                    "Alex",
                    3600,
                    emptyMap(),
                    emptyList(),
                    Date(3243432),
                    Date(3243213432),
                    Date(32434332113212),
                    GameState.LOBBY
            ),
            Game(
                    "2",
                    "Classical Game in Lausanne",
                    "Maxime",
                    7200,
                    emptyMap(),
                    participation1,
                    Date(3243432),
                    Date(3243213432),
                    Date(32434332113212),
                    GameState.ENDED
            ),
            Game(
                    "3",
                    "Battle Royale in San Francisco",
                    "JimmyG",
                    3600,
                    emptyMap(),
                    participation2,
                    Date(3243432),
                    Date(3243213432),
                    Date(32434332113212),
                    GameState.STARTED
            )
    )


    override fun getAllGames(cb: Callback<List<Game>>) {
        cb(games)
        games = games + Game(
                (games.size + 1).toString(),
                "BA"+(games.size + 1),
                "JimmyG",
                3600,
                emptyMap(),
                participation2,
                Date(3243432),
                Date(3243213432),
                Date(32434332113212),
                GameState.STARTED
        )
    }
}