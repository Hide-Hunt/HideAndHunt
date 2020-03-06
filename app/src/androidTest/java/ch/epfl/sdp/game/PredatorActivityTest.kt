package ch.epfl.sdp.game

import androidx.test.core.app.launchActivity
import org.junit.Test

class PredatorActivityTest {
    @Test
    fun activityDoesntCrash() {
        launchActivity<PredatorActivity>()
    }
}