package ch.epfl.sdp.authentication

import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ch.epfl.sdp.R
import ch.epfl.sdp.user.UserCache
import org.junit.runner.RunWith
import org.junit.*

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    val cache = UserCache()
    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun forceDisconnect() {
        User.connected = false
        cache.invalidateCache(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun canCreateActivityWithoutCrash() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.loginSubmitButton)).perform(click())
    }

    @Test
    fun registeringNewUserWorksAndConnectsIt() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("testNew@test.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.userPasswordLogin)).perform(typeText("passwordNew"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.registerSubmitButton)).perform(click())
        Assert.assertTrue(User.connected)
    }

    @Test
    fun registeringExistingUserFails() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("test0@test.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.userPasswordLogin)).perform(typeText("password0"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.registerSubmitButton)).perform(click())
        Assert.assertFalse(User.connected)
    }

    @Test
    fun rightLoginCredentialsAreAccepted() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("test0@test.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.userPasswordLogin)).perform(typeText("password0"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginSubmitButton)).perform(click())
        Assert.assertTrue(User.connected)
    }

    @Test
    fun wrongLoginCredentialsAreRejected() {
        launchActivity<LoginActivity>()
        onView(withId(R.id.userNameLogin)).perform(typeText("test0@test.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.userPasswordLogin)).perform(typeText("fewewfwe"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginSubmitButton)).perform(click())
        Assert.assertFalse(User.connected)
    }
}
