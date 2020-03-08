package ch.epfl.sdp.game

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import ch.epfl.sdp.R
import org.junit.Test

class PreyActivityTest{
    @Test
    fun dummyLiveTest() {
        launchActivity<PreyActivity>()

        Espresso.onView(ViewMatchers.withId(R.id.fullscreen_content)).perform(ViewActions.click())
    }
}