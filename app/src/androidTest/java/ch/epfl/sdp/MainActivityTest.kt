package ch.epfl.sdp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class MainActivityTest {
    @Test
    fun activityDoesntCrash() {
        launchActivity<MainActivity>()
        onView(withId(R.id.playButton)).perform(click())
    }
}