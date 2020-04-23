package ch.epfl.sdp.game;

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule
import ch.epfl.sdp.MainActivity
import ch.epfl.sdp.R
import ch.epfl.sdp.lobby.game.GameLobbyActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4::class)
class EndGameActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(EndGameActivity::class.java, false, false)

    private val startIntent = Intent()
    init {
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startIntent.putExtra("duration", 1000L)
        startIntent.putExtra("catchcount", 2)
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun backButtonShouldLaunchMainActivity() {
        activityRule.launchActivity(startIntent)
        onView(ViewMatchers.withId(R.id.btn_back_to_main)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun durationShouldMatchIntentData() {
        activityRule.launchActivity(startIntent)
        onView(ViewMatchers.withId(R.id.txt_duration_gameover)).check(matches(ViewMatchers.withText("1 seconds")))
    }

    @Test
    fun catchCountShouldMatchIntentData() {
        activityRule.launchActivity(startIntent)
        onView(ViewMatchers.withId(R.id.txt_nb_catches_gameover)).check(matches(ViewMatchers.withText("2")))
    }
}
