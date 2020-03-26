package ch.epfl.sdp.lobby

import android.app.Application

//The application Component must have the same lifecycle than the application
class MyApplication: Application(){
    val appComponent = DaggerApplicationComponent.create()!!
}

//The application Component must have the same lifecycle than the application
class MyTestApplication: Application(){
    val appComponent = DaggerTestApplicationComponent.create()!!
}