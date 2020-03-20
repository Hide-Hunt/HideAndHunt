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
import java.util.*

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

        var adapter = GameLobbyAdapter(Collections.emptyList(),0,0)
        scenario.onActivity { a ->
            adapter = a.findViewById<RecyclerView>(R.id.player_list).adapter as GameLobbyAdapter
        }

        onView(ViewMatchers.withId(R.id.faction_selection)).perform(click())
        onView(ViewMatchers.withId(R.id.player_list)).perform(swipeDown())

        var newAdapter = GameLobbyAdapter(Collections.emptyList(),0,0)
        scenario.onActivity { a ->
            newAdapter = a.findViewById<RecyclerView>(R.id.player_list).adapter as GameLobbyAdapter
        }
        assertTrue(adapter != newAdapter)
    }
}
