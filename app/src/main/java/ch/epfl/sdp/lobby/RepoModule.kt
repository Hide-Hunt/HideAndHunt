package ch.epfl.sdp.lobby

import dagger.Module
import dagger.Provides

@Module
class RepoModule {
    @Provides
    fun providesMockGameLobbyRepo(): IGameLobbyRepository{
        return MockGameLobbyRep()
    }
}