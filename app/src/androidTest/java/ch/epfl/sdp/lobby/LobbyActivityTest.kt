package ch.epfl.sdp.lobby

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.PredatorActivity
import ch.epfl.sdp.PreyActivity
import ch.epfl.sdp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LobbyActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(LobbyActivity::class.java)

    @Test
    fun testLaunchPrey() {
        launchActivity<LobbyActivity>()

        Espresso.onView(ViewMatchers.withId(R.id.startGameButton)).perform(ViewActions.click())

        //TODO : Check that the intend contains the relevant information
        intended(hasComponent(PredatorActivity::class.java.name))
    }

    @Test
    fun testLaunchPredator() {
        launchActivity<LobbyActivity>()

        Espresso.onView(ViewMatchers.withId(R.id.switch_faction)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.startGameButton)).perform(ViewActions.click())

        //TODO : Check that the intend contains the relevant information
        intended(hasComponent(PreyActivity::class.java.name))
    }
}