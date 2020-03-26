package ch.epfl.sdp.lobby

import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import ch.epfl.sdp.lobby.game.MockGameLobbyRepository
import dagger.Module
import dagger.Provides

@Module
class FakeRepoModule {
    @Provides
    fun providesMockGameLobbyRepo(): IGameLobbyRepository {
        return MockGameLobbyRepository
    }
}