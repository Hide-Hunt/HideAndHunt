package ch.epfl.sdp.lobby

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.PredatorActivity
import ch.epfl.sdp.PreyActivity
import ch.epfl.sdp.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LobbyActivityTest{

    @Test
    fun testLaunchPrey() {
        val scenario = launchActivity<LobbyActivity>()
        val mContext = InstrumentationRegistry.getInstrumentation().targetContext

        Espresso.onView(ViewMatchers.withId(R.id.startGameButton)).perform(ViewActions.click())

        assert(scenario.result.resultData.filterEquals(Intent(mContext, PreyActivity::class.java)))
    }

    @Test
    fun testLaunchPredator() {
        val scenario = launchActivity<LobbyActivity>()

        val mContext = InstrumentationRegistry.getInstrumentation().targetContext

        Espresso.onView(ViewMatchers.withId(R.id.startGameButton)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.switch_faction)).perform(ViewActions.click())

        assert(scenario.result.resultData == Intent(mContext, PredatorActivity::class.java))
    }
}