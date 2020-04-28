package ch.epfl.sdp.dagger.modules

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.user.MockUserCache
import ch.epfl.sdp.user.UserCache
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserCacheModuleTest{
    @Test
    fun testProvidesCache(){
        val userCacheModule = UserCacheModule()
        Assert.assertTrue(userCacheModule.providesUserCache() is UserCache)
    }

    @Test
    fun testMockProvidesMockUserCache(){
        val userCacheModule = FakeUserCacheModule()
        Assert.assertTrue(userCacheModule.providesUserCache() is MockUserCache)
    }
}