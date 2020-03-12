package ch.epfl.sdp.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.db.MockDB
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MockDBTest {

    @Test
    fun constructTest() {
        MockDB()
    }
}