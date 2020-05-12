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
        assertEquals(USER_COLLECTION, "users")
        assertEquals(USER_GAME_HISTORY_COLLECTION, "game_history")

        assertEquals(GAME_COLLECTION, "games")
        assertEquals(GAME_PARTICIPATION_COLLECTION, "participation")
    }
}