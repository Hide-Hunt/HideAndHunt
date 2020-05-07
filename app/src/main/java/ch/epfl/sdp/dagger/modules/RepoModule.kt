package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.lobby.game.FirebaseGameLobbyRepository
import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import ch.epfl.sdp.lobby.global.FirebaseGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import dagger.Module
import dagger.Provides

@Module
class RepoModule {
    @Provides
    fun providesGameLobbyRepo(): IGameLobbyRepository {
        return FirebaseGameLobbyRepository()
    }
    @Provides
    fun providesGlobalLobbyRepo(): IGlobalLobbyRepository{
        return FirebaseGlobalLobbyRepository()
    }
}