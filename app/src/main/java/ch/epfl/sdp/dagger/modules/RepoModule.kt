package ch.epfl.sdp.dagger.modules

import android.content.Context
import ch.epfl.sdp.lobby.game.FirebaseGameLobbyRepository
import ch.epfl.sdp.lobby.game.IGameLobbyRepository
import ch.epfl.sdp.lobby.global.FirebaseGlobalLobbyRepository
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.replay.FirebaseReplayRepository
import ch.epfl.sdp.replay.IReplayRepository
import ch.epfl.sdp.user.FirebaseUserRepo
import ch.epfl.sdp.user.IUserRepo
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi

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

    @InternalCoroutinesApi
    @Provides
    fun providesIReplayRepository(context: Context): IReplayRepository {
        return FirebaseReplayRepository(context)
    }

    @Provides
    fun providesIUserRepo(): IUserRepo {
        return FirebaseUserRepo()
    }
}