package ch.epfl.sdp.game.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.user.User
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParticipationTest {

    @Test
    fun defaultMatchesExpected() {
        val part = Participation("", false, "", 0, PlayerFaction.PREY, 0)
        assertEquals(PlayerFaction.PREY, part.faction)
        assertEquals("", part.tag)
        assertEquals(false, part.ready)
    }
}