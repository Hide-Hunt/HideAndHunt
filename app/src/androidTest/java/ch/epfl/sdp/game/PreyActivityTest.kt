package ch.epfl.sdp.game

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.R
import org.junit.Rule
import org.junit.Test

class PreyActivityTest{
    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Test
    fun dummyLiveTest() {
        launchActivity<PreyActivity>()

        onView(withId(R.id.playerID)).perform(typeText("1")).perform(closeSoftKeyboard())
        onView(withId(R.id.tracking)).perform(click())

        onView(withId(R.id.tracking)).perform(click())
        onView(withId(R.id.playerID)).perform(clearText()).perform(closeSoftKeyboard())
        onView(withId(R.id.playerID)).perform(typeText("2")).perform(closeSoftKeyboard())
        onView(withId(R.id.tracking)).perform(click())


        onView(withId(R.id.repeatLastLocation)).perform(click())
        onView(withId(R.id.tracking)).perform(click())
        onView(withId(R.id.repeatLastLocation)).perform(click())
    }
}