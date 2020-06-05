package ch.epfl.sdp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import ch.epfl.sdp.dagger.HideAndHuntTestApplication

class TestRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, HideAndHuntTestApplication::class.java.name, context)
    }
}