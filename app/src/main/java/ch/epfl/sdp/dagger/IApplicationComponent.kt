package ch.epfl.sdp.dagger

import android.content.Context
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.dagger.modules.*
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.game.GameLobbyActivity
import ch.epfl.sdp.replay.ManageReplaysActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

interface IApplicationComponent

@Singleton
@Component(modules = [RepoModule::class, UserConnectorModule::class, ReplayModule::class])
interface ApplicationComponent : IApplicationComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(activity: GameCreationActivity)
    fun inject(activity: GameLobbyActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: ManageReplaysActivity)
}

@Singleton
@Component(modules = [FakeRepoModule::class, FakeUserConnectorModule::class, ReplayModule::class])
interface TestApplicationComponent : ApplicationComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): TestApplicationComponent
    }

    override fun inject(activity: GameCreationActivity)
    override fun inject(activity: GameLobbyActivity)
    override fun inject(activity: LoginActivity)
    override fun inject(activity: ManageReplaysActivity)
}
