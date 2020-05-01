package ch.epfl.sdp.lobby.game

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ch.epfl.sdp.NFCTestHelper
import ch.epfl.sdp.R
import ch.epfl.sdp.game.NFCTagHelper.byteArrayFromHexString
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.PredatorActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.util.Log

@RunWith(AndroidJUnit4::class)
class GameLobbyActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(GameLobbyActivity::class.java, false, false)

    private val activityIntent = Intent()
    init {
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    @Test
    fun activityDoesNotCrash() {
        launchActivity<GameLobbyActivity>()
        onView(ViewMatchers.withId(R.id.swipe_container)).perform(swipeDown())
    }

    @Test
    fun swipeDownAndFactionChangeRefreshesData() {
        val scenario = launchActivity<GameLobbyActivity>()

        var baseSize = -1
        scenario.onActivity { a ->
            baseSize = a.findViewById<RecyclerView>(R.id.player_list).adapter!!.itemCount
        }

        onView(ViewMatchers.withId(R.id.faction_selection)).perform(click())
        var newSize = -1
        scenario.onActivity { a ->
            newSize = a.findViewById<RecyclerView>(R.id.player_list).adapter!!.itemCount
        }
        assertEquals(baseSize+1, newSize)

        onView(ViewMatchers.withId(R.id.player_list)).perform(swipeDown())

        scenario.onActivity { a ->
            newSize = a.findViewById<RecyclerView>(R.id.player_list).adapter!!.itemCount
        }
        assertEquals(baseSize+2, newSize)
    }

    @Test
    fun scanningTagShouldChangeReadyState() {
        val activity = activityRule.launchActivity(activityIntent)

        activity.runOnUiThread {
            activity.onFactionChange(PlayerFaction.PREY)
        }

        onView(ViewMatchers.withId(R.id.txt_player_ready)).check(matches(withText(activity.getString(R.string.you_are_not_ready))))

        val tag = NFCTestHelper.createMockTag("ABBA".byteArrayFromHexString())
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)
        activity.runOnUiThread {
            activity.onNewIntent(nfcIntent)
        }

        onView(ViewMatchers.withId(R.id.txt_player_ready)).check(matches(withText(activity.getString(R.string.you_are_ready))))
    }
}
