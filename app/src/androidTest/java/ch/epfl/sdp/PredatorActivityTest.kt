package ch.epfl.sdp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PredatorActivityTest {

    @Test
    fun activityDoesntCrash() {
        launchActivity<PredatorActivity>()
        onView(withId(R.id.fullscreen_content)).perform(click())
    }
}