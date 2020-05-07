package ch.epfl.sdp.user

import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import ch.epfl.sdp.authentication.User

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserCacheTest {
    private var cache = UserCache()

    @Before
    fun eraseCache() {
        cache.invalidateCache(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun putPutsCurrentUserInCache() {
        User.pseudo = "test"
        User.uid = "0"
        val whiteBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        User.profilePic = whiteBitmap
        cache.put(ApplicationProvider.getApplicationContext())
        User.pseudo = ""
        User.uid = ""
        User.profilePic = null
        cache.get(ApplicationProvider.getApplicationContext())
        Assert.assertTrue(User.connected)
        Assert.assertEquals("0", User.uid)
        Assert.assertEquals("test", User.pseudo)
        Assert.assertTrue(whiteBitmap.sameAs(User.profilePic))
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
