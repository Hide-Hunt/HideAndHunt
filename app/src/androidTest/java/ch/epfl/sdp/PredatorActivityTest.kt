package ch.epfl.sdp

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PredatorActivityTest {

    @Test
    fun activityDoesntCrash() {
        val scenario = launchActivity<LobbyActivity>()
    }
}