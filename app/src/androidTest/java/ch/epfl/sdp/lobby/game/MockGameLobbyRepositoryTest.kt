package ch.epfl.sdp.lobby.game

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MockGameLobbyRepositoryTest {
    private val glr = MockGameLobbyRepository

    @Test
    fun getGameIdCallbackIsCalled() {
        var called = false
        glr.getGameId { called = true }
        assertTrue(called)
    }

    @Test
    fun getGameNameCallbackIsCalled() {
        var called = false
        glr.getGameName { called = true }
        assertTrue(called)
    }

    @Test
    fun getGameDurationCallbackIsCalled() {
        var called = false
        glr.getGameDuration { called = true }
        assertTrue(called)
    }

    @Test
    fun getParticipationsCallbackIsCalled() {
        var called = false
        glr.getParticipations { called = true }
        assertTrue(called)
    }

    @Test
    fun getPlayersCallbackIsCalled() {
        var called = false
        glr.getPlayers { called = true }
        assertTrue(called)
    }

    @Test
    fun getAdminIdCallbackIsCalled() {
        var called = false
        glr.getAdminId { called = true }
        assertTrue(called)
    }

    @Test
    fun getParticipationsAddsAPlayer() {
        var nPlayers = 0
        glr.getParticipations { players -> nPlayers = players.size }
        var newNPlayers = 0
        glr.getParticipations { players -> newNPlayers = players.size }
        assertEquals(nPlayers + 1, newNPlayers)
    }

    @Test
    fun changePlayerReadyChangesReady() {
        var playerList = Collections.emptyList<Participation>()
        glr.getParticipations {
            players -> playerList = players
        }
        val player1IsReady = playerList[3].ready
        val player2IsReady = playerList[4].ready
        glr.changePlayerReady(playerList[3].user.uid)
        glr.changePlayerReady(playerList[4].user.uid)

        glr.getParticipations {
            players -> playerList = players
        }

        val newPlayer1IsReady = playerList[3].ready
        val newPlayer2IsReady = playerList[4].ready


        assertEquals(player1IsReady, !newPlayer1IsReady)
        assertEquals(player2IsReady, !newPlayer2IsReady)
    }

    @Test
    fun setPlayerFactionSetsFaction() {
        var playerList = Collections.emptyList<Participation>()
        glr.getParticipations {
            players -> playerList = players
        }

        glr.setPlayerFaction(playerList[1].user.uid, PlayerFaction.PREDATOR)
        glr.getParticipations {
            players -> playerList = players
        }
        assertEquals(playerList[1].faction, PlayerFaction.PREDATOR)

        glr.setPlayerFaction(playerList[1].user.uid, PlayerFaction.PREY)
        glr.getParticipations {
            players -> playerList = players
        }
        assertEquals(playerList[1].faction, PlayerFaction.PREY)
    }

    @Test
    fun setPlayerReadyChangesReadyness() {
        var playerList = Collections.emptyList<Participation>()
        glr.getParticipations {
            players -> playerList = players
        }

        glr.setPlayerReady(playerList[3].user.uid, false)
        glr.setPlayerReady(playerList[4].user.uid, true)

        glr.getParticipations {
            players -> playerList = players
        }

        val newPlayer1IsReady = playerList[3].ready
        val newPlayer2IsReady = playerList[4].ready


        assertEquals(false, newPlayer1IsReady)
        assertEquals(true, newPlayer2IsReady)
    }

    @Test
    fun addPlayerIsAddingANewPlayer() {
        var playerList = Collections.emptyList<Player>()
        var newID = glr.addPlayer("1234667", "TestUser")

        glr.getPlayers {
            players -> playerList = players
        }

        assertEquals(playerList.last().id, newID)
    }

    @Test
    fun removePlayerIsRemovingPlayer() {
        var playerList = Collections.emptyList<Player>()
        var newID = glr.addPlayer("1234667", "TestUser")
        glr.removePlayer(newID)

        glr.getPlayers {
            players -> playerList = players
        }

        assertNotEquals(playerList.last().id, newID)
    }

    @Test
    fun createPlayerCreatesANewPlayer() {
        var playerList = Collections.emptyList<Player>()
        var newID =glr.createPlayer("TestUser")

        glr.getPlayers {
            players -> playerList = players
        }

        assertEquals(playerList.last().id, newID)
    }
}