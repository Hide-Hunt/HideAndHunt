package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.authentication.FirebaseUserConnector
import ch.epfl.sdp.authentication.IUserConnector
import ch.epfl.sdp.authentication.MockUserConnector
import dagger.Module
import dagger.Provides

@Module
class FakeUserConnectorModule {
    @Provides
    fun providesUserConnector(): IUserConnector {
        return MockUserConnector()
    }
}