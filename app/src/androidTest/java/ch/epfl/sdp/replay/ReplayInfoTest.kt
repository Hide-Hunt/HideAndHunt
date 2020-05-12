package ch.epfl.sdp.replay

import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.game.data.Participation
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.util.*
import kotlin.NoSuchElementException

class ReplayInfoTest {
    @get:Rule
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun constructorReturnReplayInfoWithoutLocalCopy() {
        val replayInfo = ReplayInfo("g4m31d", "s0m3 g4m3", 24, 42, "stats", Faction.PREDATOR)
        assertEquals("g4m31d", replayInfo.gameID)
        assertEquals("s0m3 g4m3", replayInfo.name)
        assertEquals(24, replayInfo.startTimestamp)
        assertEquals(42, replayInfo.endTimestamp)
        assertEquals("stats", replayInfo.score)
        assertEquals(Faction.PREDATOR, replayInfo.winningFaction)
        assertEquals(false, replayInfo.localCopy)
    }

    @Test
    fun fromGameCreatesReplayInfoCorrespondingToTheGame() {
        val participation = Participation("p4rt_0f_g4m3", Faction.PREY, true, "s0m3_t4g", "3'20\"")
        val game = Game("g4m31d", "s0m3 g4m3", "4dm1n1d", 5, emptyMap(), listOf(participation), Date(24), Date(42), Date(84), GameState.ENDED)
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val localReplayStore = LocalReplayStore(ctx)
        val replayInfo = ReplayInfo.fromGame("p4rt_0f_g4m3", game, localReplayStore)

        assertEquals("g4m31d", replayInfo.gameID)
        assertEquals("s0m3 g4m3", replayInfo.name)
        assertEquals(42, replayInfo.startTimestamp)
        assertEquals(84, replayInfo.endTimestamp)
        assertEquals("3'20\"", replayInfo.score)
        assertEquals(Faction.PREY, replayInfo.winningFaction)
        assertEquals(false, replayInfo.localCopy)
    }

    @Test
    fun fromGameThrowsNoSuchElementExceptionForAbsentPlayer() {
        val participation = Participation("p4rt_0f_g4m3", Faction.PREY, true, "s0m3_t4g", "3'20\"")
        val game = Game("g4m31d", "You lost!", "4dm1n1d", 5, emptyMap(), listOf(participation), Date(), Date(), Date(), GameState.LOBBY)
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val localReplayStore = LocalReplayStore(ctx)

        exception.expect(NoSuchElementException::class.java)
        ReplayInfo.fromGame("n0t_p4rt_0f_g4m3", game, localReplayStore)
    }
}
