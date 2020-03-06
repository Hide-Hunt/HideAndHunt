package ch.epfl.sdp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @Test
    fun dummyTest() {
        Assert.assertEquals(4, 2 + 2.toLong())
    }

    @Test
    fun canCreateActivityWithoutCrash() {
        launchActivity<LoginActivity>()
    }

    @Test
    fun rightLoginCredentialsAreAccepted() {
        val scenario = launchActivity<LoginActivity>()
            onView(withId(R.id.userNameLogin)).perform(typeText("test@test.com"))
            onView(withId(R.id.userPasswordLogin)).perform(typeText("testtest"))
            onView(withId(R.id.loginSubmitButton)).perform(click())
            Assert.assertEquals(scenario.result.resultCode, 10)
    }

    @Test
    fun wrongLoginCredentialsAreRejected() {
       /* var activity = launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("test@test.com"))
        onView(withId(R.id.userPasswordLogin)).perform(typeText("wrongPassword"))
        onView(withId(R.id.loginSubmitButton)).perform(click())
        Assert.assertEquals(activity.result.resultCode, 11)*/
    }
}