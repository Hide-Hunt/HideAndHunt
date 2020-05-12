package ch.epfl.sdp.user

import org.junit.Assert.*
import org.junit.Test

class FirebaseUserRepoTest {
    @Test
    fun getUsernameShouldNotCrash() {
        val repo = FirebaseUserRepo()
        repo.getUsername("u53r1d") { Unit }
    }

    @Test
    fun addGameToHistoryShouldNotCrash() {
        val repo = FirebaseUserRepo()
        repo.addGameToHistory("u53r1d", "g4m31d")
    }

    @Test
    fun getGameHistoryShouldReturnEmptyList() {
        val repo = FirebaseUserRepo()
        var callbackCalled = false
        repo.getGameHistory("u53r1d") {
            callbackCalled = true
            assertEquals(emptyList<String>(), it)
        }
        
        for (x in 0..10) {
            if (callbackCalled) break
            Thread.sleep(100)
        }
        assertTrue(callbackCalled)
    }

    @Test
    fun getGameHistoryShouldNotCrash() {
        val repo = FirebaseUserRepo()
        repo.getGameHistory("u53r1d") { Unit }
    }
}