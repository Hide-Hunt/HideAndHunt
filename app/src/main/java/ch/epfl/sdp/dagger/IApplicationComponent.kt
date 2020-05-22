package ch.epfl.sdp.dagger

import android.content.Context
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.dagger.modules.DBModule
import ch.epfl.sdp.dagger.modules.ReplayModule
import ch.epfl.sdp.dagger.modules.RepoModule
import ch.epfl.sdp.dagger.modules.UserConnectorModule
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.game.GameLobbyActivity
import ch.epfl.sdp.lobby.global.GlobalLobbyFragment
import ch.epfl.sdp.replay.FirebaseReplayRepository
import ch.epfl.sdp.replay.LocalReplayStore
import ch.epfl.sdp.replay.ManageReplaysActivity
import ch.epfl.sdp.replay.ReplayInfoListFragment
import ch.epfl.sdp.user.ProfileActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

interface IApplicationComponent

@Singleton
@Component(modules = [DBModule::class, RepoModule::class, ReplayModule::class, UserConnectorModule::class])
interface ApplicationComponent : IApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(activity: GameCreationActivity)
    fun inject(activity: GameLobbyActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: ProfileActivity)
    fun inject(activity: GlobalLobbyFragment)
    fun inject(activity: ManageReplaysActivity)
    fun inject(activity: ReplayInfoListFragment)
    fun inject(repo: FirebaseReplayRepository)
    fun inject(store: LocalReplayStore)
}
