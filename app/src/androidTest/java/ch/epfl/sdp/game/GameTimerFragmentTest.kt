package ch.epfl.sdp.game

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GameTimerFragmentTest {

    @Test
    fun timerShowsGameOverMessageAfterTimeOut() {
        val fragmentArgs = Bundle().apply { putLong("time", 2000) }
        launchFragmentInContainer<GameTimerFragment>(fragmentArgs)
        Thread.sleep(2001)
        onView(withId(R.id.currentTime)).check(ViewAssertions.matches(withText(R.string.game_over)))
    }

}