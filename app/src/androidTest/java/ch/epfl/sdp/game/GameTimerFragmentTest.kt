package ch.epfl.sdp.game

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GameTimerFragmentTest {

    @Test
    fun timerShowsGameOverMessageAfterTimeOut() {
        val fragmentArgs = Bundle().apply { putLong("time", 1000) }
        launchFragmentInContainer<GameTimerFragment>(fragmentArgs)
        Thread.sleep(1001)
        onView(withId(R.id.currentTime)).check(ViewAssertions.matches(withText(R.string.game_over)))
    }

    @Test
    fun timerShowsNoTimeWithoutArguments() {
        launchFragmentInContainer<GameTimerFragment>()
        onView(withId(R.id.currentTime)).check(ViewAssertions.matches(withText(R.string.no_timer)))
    }

    @Test
    fun testTimeOutListener() {
        val fragmentArgs = Bundle().apply { putLong("time", 1000) }
        val scenario = launchFragmentInContainer<GameTimerFragment>(fragmentArgs)
        var callbackCalled = false
        val listener = object : GameTimerFragment.GameTimeOutListener {
            override fun onTimeOut() {
                callbackCalled = true
            }
        }
        scenario.onFragment {
            fragment -> fragment.listener = listener
            assertEquals(fragment.listener,listener)
        }
        Thread.sleep(1001)
        assertTrue(callbackCalled)
    }
}