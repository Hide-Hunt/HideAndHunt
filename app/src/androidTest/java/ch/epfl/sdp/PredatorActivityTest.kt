package ch.epfl.sdp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PredatorActivityTest {

    @Test
    fun activityDoesNotCrash() {
        launchActivity<PredatorActivity>()
        Espresso.onView(ViewMatchers.withId(R.id.fullscreen_content)).perform(ViewActions.click())
    }
}