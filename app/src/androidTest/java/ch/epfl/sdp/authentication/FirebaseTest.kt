package ch.epfl.sdp.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseTest {

    @Test
    fun disconnectingResetsSingleton() {
        val connector = FirebaseUserConnector()
        User.connected = true
        User.username = "LOL"
        Assert.assertTrue(connector.disconnect())
        Assert.assertFalse(User.connected)
    }
}