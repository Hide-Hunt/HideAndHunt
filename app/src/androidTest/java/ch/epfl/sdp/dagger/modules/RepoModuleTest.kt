package ch.epfl.sdp.dagger.modules

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.lobby.game.FirebaseGameLobbyRepository
import ch.epfl.sdp.lobby.game.MockGameLobbyRepository
import ch.epfl.sdp.lobby.global.FirebaseGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.MockGlobalLobbyRepository
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
}