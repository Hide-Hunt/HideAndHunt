package ch.epfl.sdp.db

import ch.epfl.sdp.db.FirebaseConstants.GAME_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.GAME_PARTICIPATION_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.USER_COLLECTION
import ch.epfl.sdp.db.FirebaseConstants.USER_GAME_HISTORY_COLLECTION
import org.junit.Assert.*
import org.junit.Test

class FirebaseConstantsTest {
    @Test
    fun constantsShouldHaveCorrectValues() {
        assertEquals("users", USER_COLLECTION)
        assertEquals("game_history", USER_GAME_HISTORY_COLLECTION)

        assertEquals("games", GAME_COLLECTION)
        assertEquals("participation", GAME_PARTICIPATION_COLLECTION)
    }
}