package ch.epfl.sdp.dagger

import android.app.Application
import ch.epfl.sdp.dagger.DaggerApplicationComponent
import ch.epfl.sdp.dagger.DaggerTestApplicationComponent

//The application Component must have the same lifecycle than the application
open class MyApplication: Application(){
    val appComponent: ApplicationComponent by lazy {
        initializeComponent()
    }
    open fun initializeComponent(): ApplicationComponent {
        return DaggerApplicationComponent.create()!!
    }
}

//The application Component must have the same lifecycle than the application
class MyTestApplication: MyApplication(){
    override fun initializeComponent(): ApplicationComponent {
        return DaggerTestApplicationComponent.create()!!
    }
}