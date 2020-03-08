package ch.epfl.sdp.game

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.epfl.sdp.R
import org.junit.Test

class PreyActivityTest{
    @Test
    fun dummyLiveTest() {
        launchActivity<PreyActivity>()

        onView(withId(R.id.playerID)).perform(typeText("1")).perform(closeSoftKeyboard())
        onView(withId(R.id.tracking)).perform(click())
    }
}