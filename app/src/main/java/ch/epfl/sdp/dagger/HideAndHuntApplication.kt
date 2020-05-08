package ch.epfl.sdp.dagger

import android.app.Application

//The application Component must have the same lifecycle than the application
open class HideAndHuntApplication: Application(){
    val appComponent: ApplicationComponent by lazy {
        initializeComponent()
    }
    open fun initializeComponent(): ApplicationComponent {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }
}