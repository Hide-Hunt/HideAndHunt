package ch.epfl.sdp.lobby

import dagger.Component
import javax.inject.Singleton

interface IApplicationComponent

@Singleton
@Component(modules = [RepoModule::class])
interface ApplicationComponent : IApplicationComponent {
    fun inject(activity: GameCreationActivity)
}

@Singleton
@Component(modules = [FakeRepoModule::class])
interface TestApplicationComponent : ApplicationComponent {
    override fun inject(activity: GameCreationActivity)
}
