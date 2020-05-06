package ch.epfl.sdp.dagger.modules

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.lobby.game.MockGameLobbyRepository
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoModuleTest{
    @Test
    fun testProvidesMockGameLobbyRepo(){
        val repo = RepoModule()
        assertTrue(repo.providesGameLobbyRepo() is MockGameLobbyRepository)
    }
    @Test
    fun testFakeProvidesMockGameLobbyRepo(){
        val repo = FakeRepoModule()
        assertTrue(repo.providesGameLobbyRepo() is MockGameLobbyRepository)
    }
}