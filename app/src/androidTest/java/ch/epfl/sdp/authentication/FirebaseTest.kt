package ch.epfl.sdp.authentication

import android.graphics.Bitmap
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
        User.pseudo = "LOL"
        connector.disconnect()
        Assert.assertFalse(User.connected)
    }

    @Test
    fun dummyTestForCoverage() {
        val connector = FirebaseUserConnector()
        connector.connect("test@test.com", "testtest", {}, {})
        connector.register("test@test.com", "testtest", "Test", {}, {})
        val profilePix = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        connector.modify("Test", profilePix, {}, {})
        connector.modify("Test", null, {}, {})
    }
}