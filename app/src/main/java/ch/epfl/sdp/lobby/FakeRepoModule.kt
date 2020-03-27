package ch.epfl.sdp.lobby

import ch.epfl.sdp.lobby.game.FakeMockGameLobbyRepo
import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import dagger.Module
import dagger.Provides

@Module
class FakeRepoModule {
    @Provides
    fun providesMockGameLobbyRepo(): IGameLobbyRepository {
        return FakeMockGameLobbyRepo
    }
}