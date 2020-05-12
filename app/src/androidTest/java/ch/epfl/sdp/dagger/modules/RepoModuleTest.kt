package ch.epfl.sdp.dagger.modules

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.lobby.game.FirebaseGameLobbyRepository
import ch.epfl.sdp.lobby.game.MockGameLobbyRepository
import ch.epfl.sdp.lobby.global.FirebaseGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.MockGlobalLobbyRepository
import ch.epfl.sdp.replay.FirebaseReplayRepository
import ch.epfl.sdp.replay.MockReplayRepository
import ch.epfl.sdp.user.FakeUserRepo
import ch.epfl.sdp.user.FirebaseUserRepo
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoModuleTest{
    @Test
    fun testProvidesGameLobbyRepo(){
        val repo = RepoModule()
        assertTrue(repo.providesGameLobbyRepo() is FirebaseGameLobbyRepository)
    }

    @Test
    fun testFakeProvidesGameLobbyRepo(){
        val repo = FakeRepoModule()
        assertTrue(repo.providesGameLobbyRepo() is MockGameLobbyRepository)
    }

    @Test
    fun testProvidesGlobalLobbyRepo(){
        val repo = RepoModule()
        assertTrue(repo.providesGlobalLobbyRepo() is FirebaseGlobalLobbyRepository)
    }

    @Test
    fun testFakeProvidesGlobalLobbyRepo(){
        val repo = FakeRepoModule()
        assertTrue(repo.providesGlobalLobbyRepo() is MockGlobalLobbyRepository)
    }

    @InternalCoroutinesApi
    @Test
    fun testProvidesIReplayRepository(){
        val repo = RepoModule()
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        assertTrue(repo.providesIReplayRepository(ctx) is FirebaseReplayRepository)
    }

    @Test
    fun testFakeProvidesIReplayRepository(){
        val repo = FakeRepoModule()
        assertTrue(repo.providesIReplayRepository() is MockReplayRepository)
    }

    @Test
    fun testProvidesIUserRepo(){
        val repo = RepoModule()
        assertTrue(repo.providesIUserRepo() is FirebaseUserRepo)
    }

    @Test
    fun testFakeProvidesIUserRepo(){
        val repo = FakeRepoModule()
        assertTrue(repo.providesIUserRepo() is FakeUserRepo)
    }
}