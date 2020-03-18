package ch.epfl.sdp.lobby

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class GameCreationActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(LobbyActivity::class.java)

    @Test
    fun testFillFormAndSend() {
        launchActivity<GameCreationActivity>()
        Espresso.onView(ViewMatchers.withId(R.id.create_button)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(LobbyActivity::class.java.name))
    }
}