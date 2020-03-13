package ch.epfl.sdp.game.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.lobby.PlayerParametersFragment
import ch.epfl.sdp.user.User
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParticipationTest {

    @Test
    fun defaultMatchesExpected() {
        val part = Participation(User("", 0), false, "", PlayerParametersFragment.Faction.PREY)
        assertEquals(PlayerParametersFragment.Faction.PREY, part.faction)
        assertEquals("", part.tag)
        assertEquals(false, part.ready)
    }
}