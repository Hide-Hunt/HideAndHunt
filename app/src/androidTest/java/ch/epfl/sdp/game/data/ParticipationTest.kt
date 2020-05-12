package ch.epfl.sdp.game.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParticipationTest {

    @Test
    fun defaultMatchesExpected() {
        val part = Participation("u53r1d", Faction.PREY,false, "t4g", "sc0r3")
        assertEquals("u53r1d", part.userID)
        assertEquals(Faction.PREY, part.faction)
        assertEquals(false, part.ready)
        assertEquals("t4g", part.tag)
        assertEquals("sc0r3", part.score)

        val emptyPart = Participation("u53r1d", Faction.PREY,false, "t4g", "sc0r3")
        assertEquals("", emptyPart.userID)
        assertEquals(Faction.PREDATOR, emptyPart.faction)
        assertEquals(false, emptyPart.ready)
        assertEquals("", emptyPart.tag)
        assertEquals("", emptyPart.score)

        assert(part.toPlayer(0) == Prey(0, "t4g"))
        assert(emptyPart.toPlayer(1) == Predator(1))
    }
}