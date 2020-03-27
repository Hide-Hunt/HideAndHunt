package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.lobby.game.FakeMockGameLobbyRepo
import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import ch.epfl.sdp.lobby.game.MockGameLobbyRepository
import dagger.Module
import dagger.Provides

@Module
class FakeRepoModule {
    @Provides
    fun providesMockGameLobbyRepo(): IGameLobbyRepository {
        return FakeMockGameLobbyRepo
    }
}