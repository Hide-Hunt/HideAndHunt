package ch.epfl.sdp.dagger.modules

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.authentication.FirebaseUserConnector
import ch.epfl.sdp.authentication.MockUserConnector
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserConnectorModuleTest{
    @Test
    fun testProvidesMockGameLobbyRepo(){
        val userConnectorModule = UserConnectorModule()
        assertTrue(userConnectorModule.providesUserConnector() is FirebaseUserConnector)
    }

    @Test
    fun testFakeProvidesMockGameLobbyRepo(){
        val userConnectorModule = FakeUserConnectorModule()
        assertTrue(userConnectorModule.providesUserConnector() is MockUserConnector)
    }
}