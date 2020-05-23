package ch.epfl.sdp.lobby.game

import ch.epfl.sdp.TestWait.wait
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.FirebaseConstants.INVALID_DOC_ID
import ch.epfl.sdp.db.SuccFailCallbacks.*
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.game.data.Player
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

    private fun<T> mustCallFailureCallback(test: (cb: SuccFailCallback<T>) -> Unit) {
        var callbackCalled = false

        test(SuccFailCallback({ fail("success callback called") }, { callbackCalled = true }))

        wait(1000, {callbackCalled})
        assertTrue(callbackCalled)
    }

    @Test
    fun addLocalParticipationShouldFailWithInvalidGameID() {
        mustCallFailureCallback<Unit> { cb ->
            repo.addLocalParticipation(INVALID_DOC_ID, UnitSuccFailCallback({ cb.success(Unit) }, cb.failure))
        }
    }

    @Test
    fun getGameNameShouldFailWithInvalidGameID() {
        mustCallFailureCallback<String> { cb ->
            repo.getGameName(INVALID_DOC_ID, cb)
        }
    }

    @Test
    fun getGameDurationShouldFailWithInvalidGameID() {
        mustCallFailureCallback<Long> { cb ->
            repo.getGameDuration(INVALID_DOC_ID, cb)
        }
    }

    @Test
    fun getPlayersShouldFailWithInvalidGameID() {
        mustCallFailureCallback<List<Player>> { cb ->
            repo.getPlayers(INVALID_DOC_ID, cb)
        }
    }

    @Test
    fun getParticipationShouldFailWithInvalidGameID() {
        mustCallFailureCallback<List<Participation>> { cb ->
            repo.getParticipation(INVALID_DOC_ID, cb)
        }
    }

    @Test
    fun getAdminIdShouldFailWithInvalidGameID() {
        mustCallFailureCallback<String> { cb ->
            repo.getAdminId(INVALID_DOC_ID, cb)
        }
    }

    @Test
    fun requestGameLaunchShouldFailWithInvalidGameID() {
        mustCallFailureCallback<Unit> { cb ->
            repo.requestGameLaunch(INVALID_DOC_ID, UnitSuccFailCallback({ cb.success(Unit) }, cb.failure))
        }
    }

    @Test
    fun setPlayerReadyShouldFailWithInvalidGameID() {
        mustCallFailureCallback<Unit> { cb ->
            repo.setPlayerReady(INVALID_DOC_ID, "", true, UnitSuccFailCallback({ cb.success(Unit) }, cb.failure))
        }
    }

    @Test
    fun setPlayerFactionShouldFailWithInvalidGameID() {
        mustCallFailureCallback<Unit> { cb ->
            repo.setPlayerFaction(INVALID_DOC_ID, "", Faction.PREY, UnitSuccFailCallback({ cb.success(Unit) }, cb.failure))
        }
    }

    @Test
    fun setPlayerTagShouldFailWithInvalidGameID() {
        mustCallFailureCallback<Unit> { cb ->
            repo.setPlayerTag(INVALID_DOC_ID, "", "", UnitSuccFailCallback({ cb.success(Unit) }, cb.failure))
        }
    }

    @Test
    fun removeLocalParticipationShouldFailWithInvalidGameID() {
        mustCallFailureCallback<Unit> { cb ->
            repo.removeLocalParticipation(INVALID_DOC_ID, UnitSuccFailCallback({ cb.success(Unit) }, cb.failure))
        }
    }
}