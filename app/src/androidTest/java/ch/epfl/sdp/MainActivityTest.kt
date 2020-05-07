package ch.epfl.sdp

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.authentication.User
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import ch.epfl.sdp.user.MockUserCache
import ch.epfl.sdp.user.ProfileActivity
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java, false, false)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun globalLobbyCanBeClicked() {
        launchActivity<MainActivity>()
        onView(withId(R.id.playButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(GlobalLobbyActivity::class.java.name))
    }

    @Test
    fun loginActivityCanBeClicked() {
        launchActivity<MainActivity>()
        onView(withId(R.id.loginButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }

    @Test
    fun debugActivityCanBeClicked() {
        launchActivity<MainActivity>()
        onView(withId(R.id.btn_debug)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(DebugActivity::class.java.name))
    }

    @Test
    fun profileActivityIsNotShowedAtStartup() {
        MockUserCache.resetCache()
        launchActivity<MainActivity>()
        onView(withId(R.id.profileButton)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun profileActivityCanBeClicked() {
        User.connected = true
        User.pseudo = "test"
        User.uid = "0"
        MockUserCache.fakeCache()
        launchActivity<MainActivity>()
        onView(withId(R.id.profileButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
    }

    @Test
    fun onRestartWithConnectedUserProfileButtonShows() {
        User.connected = true
        User.pseudo = "test"
        User.uid = "0"
        MockUserCache.fakeCache()
        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.recreate()

        onView(withId(R.id.profileButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}