package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.authentication.IUserConnector
import ch.epfl.sdp.authentication.MockUserConnector
import ch.epfl.sdp.user.IUserCache
import ch.epfl.sdp.user.MockUserCache
import dagger.Module
import dagger.Provides

@Module
class FakeUserCacheModule {
    @Provides
    fun providesUserCache(): IUserCache {
        return MockUserCache
    }
}