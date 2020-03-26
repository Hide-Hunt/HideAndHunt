package ch.epfl.sdp.lobby

import dagger.Module
import dagger.Provides

@Module
class FakeRepoModule {
    @Provides
    fun providesMockGameLobbyRepo(): IGameLobbyRepository{
        return MockGameLobbyRep()
    }
}