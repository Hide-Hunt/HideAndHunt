package ch.epfl.sdp.user

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import ch.epfl.sdp.authentication.User
import java.nio.charset.Charset

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class MockUserTest {
    private var cache = MockUserCache

    @Before
    fun eraseCache() {
        cache.resetCache()
    }

    @Test
    fun putPutsCurrentUserInCache() {
        User.pseudo = "test"
        User.uid = "0"
        User.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        val split = cache.toString().split(":")
        Assert.assertEquals("test", split[0])
        Assert.assertEquals("0", split[1])
    }

    @Test
    fun getGetsUserFromCache() {
        User.pseudo = "test"
        User.uid = "0"
        User.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        User.pseudo = ""
        User.uid = ""
        User.connected = false
        cache.get(ApplicationProvider.getApplicationContext())
        Assert.assertEquals("test", User.pseudo)
        Assert.assertEquals("0", User.uid)
        Assert.assertTrue(User.connected)
    }

    @Test
    fun getUserFromEmptyCache() {
        User.connected = true
        cache.get(ApplicationProvider.getApplicationContext())
        Assert.assertFalse(User.connected)
    }
}
