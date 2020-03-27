package ch.epfl.sdp.lobby

import android.app.Application

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
    override fun initializeComponent(): TestApplicationComponent {
        return DaggerTestApplicationComponent.create()!!
    }
}