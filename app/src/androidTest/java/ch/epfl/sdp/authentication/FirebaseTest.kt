package ch.epfl.sdp.authentication

import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.user.UserCache
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseTest {

    @Test
    fun disconnectingResetsSingleton() {
        val connector = FirebaseUserConnector()
        LocalUser.connected = true
        LocalUser.email = "LOL"
        connector.disconnect()
        Assert.assertFalse(LocalUser.connected)
    }

    @Test
    fun firebaseOperationsCreatesCache() {
        val connector = FirebaseUserConnector()
        connector.connect("test@test.com", "testtest", {}, {})
        connector.register("test@test.com", "testtest", "Test", {}, {})
        val profilePix = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        connector.modify("Test", profilePix, {}, {})
        connector.modify("Test", null, {}, {})
        Assert.assertTrue(UserCache().doesExist(ApplicationProvider.getApplicationContext()))
    }
}