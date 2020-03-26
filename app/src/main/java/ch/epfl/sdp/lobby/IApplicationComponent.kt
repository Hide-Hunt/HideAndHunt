package ch.epfl.sdp.lobby

import dagger.Component

interface IApplicationComponent

@Component(modules = [RepoModule::class])
interface ApplicationComponent : IApplicationComponent {
    fun inject(activity: GameCreationActivity)
}

@Component(modules = [RepoModule::class])
interface TestApplicationComponent : IApplicationComponent {
    fun inject(activity: GameCreationActivity)
}
