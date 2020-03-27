package ch.epfl.sdp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
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
}