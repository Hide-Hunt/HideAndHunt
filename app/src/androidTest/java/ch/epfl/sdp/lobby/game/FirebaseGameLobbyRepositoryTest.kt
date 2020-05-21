package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.TestWait.wait
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.FirebaseConstants.INVALID_DOC_ID
import ch.epfl.sdp.db.UnitCallback
import ch.epfl.sdp.game.data.Faction
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class FirebaseGameLobbyRepositoryTest {
    private lateinit var repo : FirebaseGameLobbyRepository

    init {
        LocalUser.uid = "u52r1d"
    }

    @Before
    fun setup() {
        repo = FirebaseGameLobbyRepository()
    }

    private fun mustCallFailureCallback(test: (succCb: Callback<Any>, failCb: UnitCallback) -> Unit) {
        var callbackCalled = false

        test({ fail("success callback called") }, { callbackCalled = true })

        wait(1000, {callbackCalled})
        assertTrue(callbackCalled)
    }

    @Test
    fun addLocalParticipationShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.addLocalParticipation(INVALID_DOC_ID, { succCb(Unit) }, failCb)
        }
    }

    @Test
    fun getGameNameShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.getGameName(INVALID_DOC_ID, succCb, failCb)
        }
    }

    @Test
    fun getGameDurationShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.getGameDuration(INVALID_DOC_ID, succCb, failCb)
        }
    }

    @Test
    fun getPlayersShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.getPlayers(INVALID_DOC_ID, succCb, failCb)
        }
    }

    @Test
    fun getParticipationsShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.getParticipations(INVALID_DOC_ID, succCb, failCb)
        }
    }

    @Test
    fun getAdminIdShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.getAdminId(INVALID_DOC_ID, succCb, failCb)
        }
    }

    @Test
    fun requestGameLaunchShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.requestGameLaunch(INVALID_DOC_ID, { succCb(Unit) }, failCb)
        }
    }

    @Test
    fun setPlayerReadyShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.setPlayerReady(INVALID_DOC_ID, "", true, { succCb(Unit) }, failCb)
        }
    }

    @Test
    fun setPlayerFactionShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.setPlayerFaction(INVALID_DOC_ID, "", Faction.PREY, { succCb(Unit) }, failCb)
        }
    }

    @Test
    fun setPlayerTagShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.setPlayerTag(INVALID_DOC_ID, "", "", { succCb(Unit) }, failCb)
        }
    }

    @Test
    fun removeLocalParticipationShouldFailWithInvalidGameID() {
        mustCallFailureCallback { succCb, failCb ->
            repo.removeLocalParticipation(INVALID_DOC_ID, { succCb(Unit) }, failCb)
        }
    }
}