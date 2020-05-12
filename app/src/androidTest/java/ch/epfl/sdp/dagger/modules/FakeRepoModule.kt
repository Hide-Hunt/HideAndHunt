package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import ch.epfl.sdp.lobby.game.MockGameLobbyRepository
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.MockGlobalLobbyRepository
import ch.epfl.sdp.replay.IReplayRepository
import ch.epfl.sdp.replay.MockReplayRepository
import ch.epfl.sdp.user.FakeUserRepo
import ch.epfl.sdp.user.IUserRepo
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

    @Provides
    fun providesIReplayRepository(): IReplayRepository {
        return MockReplayRepository()
    }

    @Provides
    fun providesIUserRepo(): IUserRepo {
        return FakeUserRepo()
    }
}