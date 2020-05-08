package ch.epfl.sdp.user

import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import ch.epfl.sdp.authentication.LocalUser

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
        LocalUser.pseudo = "test"
        LocalUser.uid = "0"
        val whiteBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        LocalUser.profilePic = whiteBitmap
        cache.put(ApplicationProvider.getApplicationContext())
        LocalUser.pseudo = ""
        LocalUser.uid = ""
        LocalUser.profilePic = null
        cache.get(ApplicationProvider.getApplicationContext())
        Assert.assertTrue(LocalUser.connected)
        Assert.assertEquals("0", LocalUser.uid)
        Assert.assertEquals("test", LocalUser.pseudo)
        Assert.assertTrue(whiteBitmap.sameAs(LocalUser.profilePic))
    }

    @Test
    fun getGetsUserFromCache() {
        LocalUser.pseudo = "test"
        LocalUser.uid = "0"
        LocalUser.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        LocalUser.pseudo = ""
        LocalUser.uid = ""
        LocalUser.connected = false
        cache.get(ApplicationProvider.getApplicationContext())
        Assert.assertEquals("test", LocalUser.pseudo)
        Assert.assertEquals("0", LocalUser.uid)
        Assert.assertTrue(LocalUser.connected)
    }

    @Test
    fun getUserFromEmptyCache() {
        LocalUser.connected = true
        cache.get(ApplicationProvider.getApplicationContext())
        Assert.assertFalse(LocalUser.connected)
    }
}
