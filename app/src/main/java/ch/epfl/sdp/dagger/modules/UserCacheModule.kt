package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.user.IUserCache
import ch.epfl.sdp.user.UserCache
import dagger.Module
import dagger.Provides


@Module
class UserCacheModule {
    @Provides
    fun providesUserCache(): IUserCache {
        return UserCache()
    }
}