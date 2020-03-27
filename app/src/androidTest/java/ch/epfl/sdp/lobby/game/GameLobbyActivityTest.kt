package ch.epfl.sdp.lobby.game

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameLobbyActivityTest {

    @Test
    fun activityDoesNotCrash() {
        launchActivity<GameLobbyActivity>()
        onView(ViewMatchers.withId(R.id.swipe_container)).perform(swipeDown())
    }

    @Test
    fun swipeDownRefreshesData() {
        val scenario = launchActivity<GameLobbyActivity>()

        var size = -1
        scenario.onActivity { a ->
            size = a.findViewById<RecyclerView>(R.id.player_list).adapter!!.itemCount
        }
        assertEquals(11,size)

        onView(ViewMatchers.withId(R.id.faction_selection)).perform(click())
        onView(ViewMatchers.withId(R.id.player_list)).perform(swipeDown())

        var newSize = -1
        scenario.onActivity { a ->
            newSize = a.findViewById<RecyclerView>(R.id.player_list).adapter!!.itemCount
        }
        assertEquals(12,newSize)
    }
}
