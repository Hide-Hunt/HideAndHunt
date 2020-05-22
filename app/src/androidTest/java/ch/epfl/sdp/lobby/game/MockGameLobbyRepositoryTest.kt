package ch.epfl.sdp.lobby.game

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.db.SuccFailCallbacks.*
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MockGameLobbyRepositoryTest {
    private val glr = MockGameLobbyRepository

    @Test
    fun getGameNameCallbackIsCalled() {
        var called = false
        glr.getGameName("0", SuccFailCallback({ called = true }))
        assertTrue(called)
    }

    @Test
    fun getGameDurationCallbackIsCalled() {
        var called = false
        glr.getGameDuration("0", SuccFailCallback({ called = true }))
        assertTrue(called)
    }

    @Test
    fun getParticipationCallbackIsCalled() {
        var called = false
        glr.getParticipation("0", SuccFailCallback({ called = true }))
        assertTrue(called)
    }

    @Test
    fun getPlayersCallbackIsCalled() {
        var called = false
        glr.getPlayers("0", SuccFailCallback({ called = true }))
        assertTrue(called)
    }

    @Test
    fun getAdminIdCallbackIsCalled() {
        var called = false
        glr.getAdminId("0", SuccFailCallback({ called = true }))
        assertTrue(called)
    }

    @Test
    fun setPlayerFactionSetsFaction() {
        var playerList = Collections.emptyList<Participation>()
        glr.getParticipation("0", SuccFailCallback({ playerList = it }))

        glr.setPlayerFaction("0", playerList[1].userID, Faction.PREDATOR, UnitSuccFailCallback())
        glr.getParticipation("0", SuccFailCallback({ playerList = it }))
        assertEquals(playerList[1].faction, Faction.PREDATOR)

        glr.setPlayerFaction("0", playerList[1].userID, Faction.PREY, UnitSuccFailCallback())
        glr.getParticipation("0", SuccFailCallback({ playerList = it }))
        assertEquals(playerList[1].faction, Faction.PREY)
    }

    @Test
    fun setPlayerReadyChangesReadiness() {
        var playerList = Collections.emptyList<Participation>()
        glr.getParticipation("0", SuccFailCallback({ playerList = it }))

        glr.setPlayerReady("0", playerList[3].userID, false, UnitSuccFailCallback())
        glr.setPlayerReady("0", playerList[4].userID, true, UnitSuccFailCallback())

        glr.getParticipation("0", SuccFailCallback({ playerList = it }))

        val newPlayer1IsReady = playerList[3].ready
        val newPlayer2IsReady = playerList[4].ready


        assertEquals(false, newPlayer1IsReady)
        assertEquals(true, newPlayer2IsReady)
    }
}