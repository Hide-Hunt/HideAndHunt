package ch.epfl.sdp.lobby

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import ch.epfl.sdp.dagger.HideAndHuntTestApplication

class MyCustomTestRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, HideAndHuntTestApplication::class.java.name, context)
    }
}