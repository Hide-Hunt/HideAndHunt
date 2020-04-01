package ch.epfl.sdp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class DebugActivityTest{
    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Test
    fun dummyLiveTest() {
        launchActivity<DebugActivity>()

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

    @Test
    fun trackingShouldBeDisabledIfNoPlayerID() {
        launchActivity<DebugActivity>()
        onView(withId(R.id.tracking)).check(matches(not(isEnabled())))

        onView(withId(R.id.playerID)).perform(typeText("1")).perform(closeSoftKeyboard())
        onView(withId(R.id.tracking)).check(matches(isEnabled()))

        onView(withId(R.id.playerID)).perform(clearText()).perform(closeSoftKeyboard())
        onView(withId(R.id.tracking)).check(matches(not(isEnabled())))
    }

    @Test
    fun playerIDShouldBeDisabledWhenTrackingIsEnabled() {
        launchActivity<DebugActivity>()
        onView(withId(R.id.playerID)).check(matches(isEnabled()))

        onView(withId(R.id.playerID)).perform(typeText("1")).perform(closeSoftKeyboard())
        onView(withId(R.id.playerID)).check(matches(isEnabled()))

        onView(withId(R.id.tracking)).perform(click())
        onView(withId(R.id.playerID)).check(matches(not(isEnabled())))

        onView(withId(R.id.tracking)).perform(click())
        onView(withId(R.id.playerID)).check(matches(isEnabled()))
    }

    @Test
    fun initialNumberOfUpdatesShouldBeZero() {
        launchActivity<DebugActivity>()
        onView(withId(R.id.updateNb)).check(matches(withText("0")))
    }
}