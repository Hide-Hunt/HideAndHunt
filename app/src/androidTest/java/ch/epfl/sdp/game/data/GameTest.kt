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
        val game = Game("g4m31d", "You lost!", "4dm1n1d", 5, emptyMap(), emptyList(), Date(), Date(), Date(), GameState.LOBBY)
        assertEquals("g4m31d", game.id)
        assertEquals("You lost!", game.name)
        assertEquals(5, game.duration)
        assertTrue(game.participation.isEmpty())
        assertEquals("4dm1n1d", game.adminID)
        assertTrue(game.params.isEmpty())
        assertTrue(game.creationDate.after(Date(0)))
        assertTrue(game.startDate.after(Date(0)))
        assertTrue(game.endDate.after(Date(0)))
        assertEquals(GameState.LOBBY, game.state)
    }
}