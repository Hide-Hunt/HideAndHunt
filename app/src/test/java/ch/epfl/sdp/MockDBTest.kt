package ch.epfl.sdp

import ch.epfl.sdp.db.MockDB
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class MockDBTest {

    @Test
    fun callbackIsCalled() {
        val db = MockDB()
        var called = false
        db.getAllGames { games -> called = true }
        assertEquals(true, called)
    }

    @Test
    fun gameAreAdded() {
        val db = MockDB()
        var prev = 0
        db.getAllGames { games -> prev = games.size }
        db.addGame(Game(10,"", "", 0, emptyMap(), GameState.LOBBY, emptyList(), Date(), Date(), Date())) { ok -> Unit}
        var new = 0
        db.getAllGames { games -> new = games.size }
        assertEquals(prev + 1, new)
    }

    @Test
    fun correctGameIsAdded() {
        val db = MockDB()
        db.getAllGames {}
        val newGame = Game(10,"", "", 0, emptyMap(), GameState.LOBBY, emptyList(), Date(), Date(), Date())
        db.addGame(newGame) { ok -> Unit}
        var gGames: List<Game>? = null
        db.getAllGames { games -> gGames = games }
        assertEquals(true, gGames!!.contains(newGame))
    }

}