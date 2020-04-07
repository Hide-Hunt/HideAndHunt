package ch.epfl.sdp.dagger

import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.dagger.modules.FakeRepoModule
import ch.epfl.sdp.dagger.modules.FakeUserConnectorModule
import ch.epfl.sdp.dagger.modules.RepoModule
import ch.epfl.sdp.dagger.modules.UserConnectorModule
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.game.GameLobbyActivity
import dagger.Component
import javax.inject.Singleton

interface IApplicationComponent

@Singleton
@Component(modules = [RepoModule::class, UserConnectorModule::class])
interface ApplicationComponent : IApplicationComponent {
    fun inject(activity: GameCreationActivity)
    fun inject(activity: GameLobbyActivity)
    fun inject(activity: LoginActivity)
}

@Singleton
@Component(modules = [FakeRepoModule::class, FakeUserConnectorModule::class])
interface TestApplicationComponent : ApplicationComponent {
    override fun inject(activity: GameCreationActivity)
    override fun inject(activity: GameLobbyActivity)
    override fun inject(activity: LoginActivity)
}
