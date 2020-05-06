package ch.epfl.sdp.dagger

//The application Component must have the same lifecycle than the application
class HideAndHuntTestApplication: HideAndHuntApplication(){
    override fun initializeComponent(): ApplicationComponent {
        return DaggerTestApplicationComponent.factory().create(applicationContext)
    }
}