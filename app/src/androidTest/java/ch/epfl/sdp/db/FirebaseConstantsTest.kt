package ch.epfl.sdp.db

import org.junit.Assert.*
import org.junit.Test

class FirebaseConstantsTest {
    @Test
    fun constantsShouldHaveCorrectValues() {
        val fbConstant = FirebaseConstants
        assertEquals("users", fbConstant.USER_COLLECTION)
        assertEquals("game_history", fbConstant.USER_GAME_HISTORY_COLLECTION)

        assertEquals("games", fbConstant.GAME_COLLECTION)
        assertEquals("participation", fbConstant.GAME_PARTICIPATION_COLLECTION)
    }
}