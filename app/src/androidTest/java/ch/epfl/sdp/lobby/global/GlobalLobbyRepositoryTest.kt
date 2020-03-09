package ch.epfl.sdp.lobby.global

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.db.MockDB
import ch.epfl.sdp.lobby.global.GlobalLobbyRepository
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GlobalLobbyRepositoryTest {

    @Test
    fun callbackIsCalled() {
        val glr = GlobalLobbyRepository(MockDB())
        var called = false
        glr.getAllGames { games -> called = true }
        assertEquals(true,  called)
    }
}