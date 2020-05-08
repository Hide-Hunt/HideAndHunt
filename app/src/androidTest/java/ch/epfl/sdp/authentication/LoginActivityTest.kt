package ch.epfl.sdp.authentication

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ch.epfl.sdp.R
import org.junit.runner.RunWith
import org.junit.*

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    private val mockConnector = MockUserConnector()

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun forceDisconnect() {
        LocalUser.connected = false
    }

    @Test
    fun canCreateActivityWithoutCrash() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.loginSubmitButton)).perform(click())
    }

    @Test
    fun registeringNewUserWorksAndConnectsIt() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("testnew@test.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.userPasswordLogin)).perform(typeText("passwordNew"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.registerSubmitButton)).perform(click())
        onView(withId(R.id.loginTextResult)).check(ViewAssertions.matches(withText("Account created and logged in as testnew@test.com")))
        Assert.assertTrue(LocalUser.connected)
    }

    @Test
    fun registeringExistingUserFails() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("test0@test.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.userPasswordLogin)).perform(typeText("password0"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.registerSubmitButton)).perform(click())
        onView(withId(R.id.loginTextResult)).check(ViewAssertions.matches(withText("Account creation failed")))
        Assert.assertFalse(LocalUser.connected)
    }

    @Test
    fun rightLoginCredentialsAreAccepted() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("test0@test.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.userPasswordLogin)).perform(typeText("password0"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginSubmitButton)).perform(click())
        onView(withId(R.id.loginTextResult)).check(ViewAssertions.matches(withText("User logged in as test0@test.com")))
        Assert.assertTrue(LocalUser.connected)
    }

    @Test
    fun wrongLoginCredentialsAreRejected() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("test0@test.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.userPasswordLogin)).perform(typeText("fewewfwe"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginSubmitButton)).perform(click())
        onView(withId(R.id.loginTextResult)).check(ViewAssertions.matches(withText("Logging failed")))
        Assert.assertFalse(LocalUser.connected)
    }
}
