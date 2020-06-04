package ch.epfl.sdp

import android.graphics.Bitmap
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.lobby.GameCreationActivity
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import ch.epfl.sdp.user.ProfileActivity
import ch.epfl.sdp.user.UserCache
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    private val cache = UserCache()
    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java, false, false)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
        cache.invalidateCache(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun globalLobbyCanBeClicked() {
        LocalUser.connected = true
        LocalUser.pseudo = "test"
        LocalUser.uid = "0"
        LocalUser.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        launchActivity<MainActivity>()
        onView(withId(R.id.playButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(GlobalLobbyActivity::class.java.name))
    }

    @Test
    fun newGameCanBeClicked() {
        LocalUser.connected = true
        LocalUser.pseudo = "test"
        LocalUser.uid = "0"
        LocalUser.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        launchActivity<MainActivity>()
        onView(withId(R.id.newGame_button)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(GameCreationActivity::class.java.name))
    }

    @Test
    fun loginActivityCanBeClicked() {
        launchActivity<MainActivity>()
        onView(withId(R.id.loginButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }

    @Test
    fun playButtonIsDisabledIfUserNotConnected() {
        LocalUser.connected = false
        val activity = launchActivity<MainActivity>()
        onView(withId(R.id.playButton)).check(matches(not(isEnabled())))
        LocalUser.connected = true
        LocalUser.pseudo = "test"
        LocalUser.uid = "0"
        LocalUser.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        activity.recreate()
        onView(withId(R.id.playButton)).check(matches(isEnabled()))
    }

    @Test
    fun newGameButtonIsDisabledIfUserNotConnected() {
        LocalUser.connected = false
        val activity = launchActivity<MainActivity>()
        onView(withId(R.id.newGame_button)).check(matches(not(isEnabled())))
        LocalUser.connected = true
        LocalUser.pseudo = "test"
        LocalUser.uid = "0"
        LocalUser.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        activity.recreate()
        onView(withId(R.id.newGame_button)).check(matches(isEnabled()))
    }

    @Test
    fun profileActivityIsNotShowedAtStartup() {
        launchActivity<MainActivity>()
        onView(withId(R.id.profileButton)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun profileActivityCanBeClicked() {
        LocalUser.connected = true
        LocalUser.pseudo = "test"
        LocalUser.uid = "0"
        LocalUser.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        launchActivity<MainActivity>()
        onView(withId(R.id.profileButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
    }

    @Test
    fun onRestartWithConnectedUserProfileButtonShows() {
        LocalUser.connected = true
        LocalUser.pseudo = "test"
        LocalUser.uid = "0"
        LocalUser.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        cache.put(ApplicationProvider.getApplicationContext())
        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.recreate()

        onView(withId(R.id.profileButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}