package ch.epfl.sdp.dagger

import ch.epfl.sdp.MainActivity
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.dagger.modules.*
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.game.GameLobbyActivity
import ch.epfl.sdp.user.ProfileActivity
import dagger.Component
import javax.inject.Singleton

interface IApplicationComponent

@Singleton
@Component(modules = [RepoModule::class, UserConnectorModule::class])
interface ApplicationComponent : IApplicationComponent {
    fun inject(activity: GameCreationActivity)
    fun inject(activity: GameLobbyActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: ProfileActivity)
}

@Singleton
@Component(modules = [FakeRepoModule::class, FakeUserConnectorModule::class])
interface TestApplicationComponent : ApplicationComponent {
    override fun inject(activity: GameCreationActivity)
    override fun inject(activity: GameLobbyActivity)
    override fun inject(activity: LoginActivity)
    override fun inject(activity: MainActivity)
    override fun inject(activity: ProfileActivity)
}
