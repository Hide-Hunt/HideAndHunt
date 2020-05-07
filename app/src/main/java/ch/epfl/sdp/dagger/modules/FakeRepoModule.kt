package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import ch.epfl.sdp.lobby.game.MockGameLobbyRepository
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.MockGlobalLobbyRepository
import dagger.Module
import dagger.Provides

@Module
class FakeRepoModule {
    @Provides
    fun providesGameLobbyRepo(): IGameLobbyRepository {
        return MockGameLobbyRepository
    }
    @Provides
    fun providesGlobalLobbyRepo(): IGlobalLobbyRepository {
        return MockGlobalLobbyRepository()
    }
}