package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.lobby.game.FirebaseGameLobbyRepository
import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import ch.epfl.sdp.lobby.game.MockGameLobbyRepository
import dagger.Module
import dagger.Provides

@Module
class RepoModule {
    @Provides
    fun providesGameLobbyRepo(): IGameLobbyRepository {
        return FirebaseGameLobbyRepository()
    }
}