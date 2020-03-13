package ch.epfl.sdp.lobby.global

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.db.MockDB
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MockGlobalLobbyRepositoryTest {

    @Test
    fun callbackIsCalled() {
        val glr = MockGlobalLobbyRepository(MockDB())
        var called = false
        glr.getAllGames { called = true }
        assertEquals(true, called)
    }

    @Test
    fun moreThanZeroGames() {
        val glr = MockGlobalLobbyRepository(MockDB())
        var size = 0
        glr.getAllGames { games -> size = games.size }
        assertTrue(size > 0)
    }
}