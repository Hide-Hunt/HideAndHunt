package ch.epfl.sdp.lobby.game

import android.widget.Button
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.lobby.PlayerParametersFragment
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class GameLobbyActivityTest {

    @Test
    fun activityDoesNotCrash() {
        val scenario = launchActivity<GameLobbyActivity>()
        Espresso.onView(ViewMatchers.withId(R.id.swipe_container)).perform(ViewActions.swipeDown())
    }
}