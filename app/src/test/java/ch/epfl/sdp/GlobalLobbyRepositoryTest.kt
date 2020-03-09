package ch.epfl.sdp

import ch.epfl.sdp.db.MockDB
import ch.epfl.sdp.lobby.global.GlobalLobbyRepository
import org.junit.Assert.*
import org.junit.Test

class GlobalLobbyRepositoryTest {

    @Test
    fun callbackIsCalled() {
        val glr = GlobalLobbyRepository(MockDB())
        var called = false
        glr.getAllGames { games -> called = true }
        assertEquals(true,  called)
    }
}