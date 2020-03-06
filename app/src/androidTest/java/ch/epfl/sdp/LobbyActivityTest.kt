package ch.epfl.sdp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class LobbyActivityTest {
    @Test
    fun activityDoesntCrash() {
        launchActivity<LobbyActivity>()
        onView(withId(R.id.startGameButton)).perform(ViewActions.click())
    }
}