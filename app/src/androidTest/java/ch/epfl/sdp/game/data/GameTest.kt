package ch.epfl.sdp.game.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class GameTest {

    @Test
    fun defaultValuesMatchExpectation() {
        val game = Game(0, "", "", 0, emptyMap(), GameState.LOBBY, emptyList(), Date(), Date(), Date(), 0)
        assertEquals(0, game.id)
        assertEquals("", game.admin)
        assertEquals("", game.name)
        assertEquals(0, game.duration)
        assertEquals(GameState.LOBBY, game.state)
        assertTrue(game.participation.isEmpty())
        assertEquals(0, game.adminID)
        assertTrue(game.params.isEmpty())
        assertTrue(game.startDate.after(Date(0)))
        assertTrue(game.creationDate.after(Date(0)))
        assertTrue(game.endDate.after(Date(0)))
    }
}