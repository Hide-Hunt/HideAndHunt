package ch.epfl.sdp.game.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameTest {

    @Test
    fun defaultValuesMatchExpectation() {
        val game = Game()
        assertEquals(0, game.id)
        assertEquals("", game.admin)
        assertEquals("", game.name)
        assertEquals(0, game.duration)
        assertTrue(game.params is HashMap)
        assertEquals(GameState.LOBBY, game.state)
        assertTrue(game.participation.isEmpty())
    }
}