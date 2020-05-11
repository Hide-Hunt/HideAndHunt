package ch.epfl.sdp.lobby.game

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ch.epfl.sdp.EspressoViewAssertions.RecyclerViewItemCount
import ch.epfl.sdp.NFCTestHelper
import ch.epfl.sdp.R
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.game.NFCTagHelper.byteArrayFromHexString
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameLobbyActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(GameLobbyActivity::class.java, false, false)

    private val activityIntent = Intent()

    @Before
    fun setup() {
        activityIntent.putExtra("gameID", "g4m31d")
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        LocalUser.uid = "u53r1d"
    }

    @Test
    fun activityDoesNotCrash() {
        activityRule.launchActivity(activityIntent)
        onView(withId(R.id.swipe_container)).perform(swipeDown())
    }

    @Test
    fun swipeDownAndFactionChangeRefreshesData() {
        val activity = activityRule.launchActivity(activityIntent)
        val repo = activity.repository as MockGameLobbyRepository

        // Initially 11 players
        onView(withId(R.id.player_list)).check(RecyclerViewItemCount(11))

        // Add one then change faction => should reload
        repo.players.add(Participation("Player11", Faction.PREY, false, "0ABC", ""))
        onView(withId(R.id.faction_selection)).perform(click())
        onView(withId(R.id.player_list)).check(RecyclerViewItemCount(12))

        // Add one then swipe => should reload again
        repo.players.add(Participation("Player12", Faction.PREY, false, "0ABC", ""))
        onView(withId(R.id.player_list)).perform(swipeDown())
        onView(withId(R.id.player_list)).check(RecyclerViewItemCount(13))
    }

    @Test
    fun scanningTagShouldChangeReadyState() {
        val activity = activityRule.launchActivity(activityIntent)

        activity.runOnUiThread {
            activity.onFactionChange(Faction.PREY)
        }

        onView(withId(R.id.txt_player_ready)).check(matches(withText(activity.getString(R.string.you_are_not_ready))))

        val tag = NFCTestHelper.createMockTag("ABBA".byteArrayFromHexString())
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)
        activity.runOnUiThread {
            activity.onNewIntent(nfcIntent)
        }

        onView(withId(R.id.txt_player_ready)).check(matches(withText(activity.getString(R.string.you_are_ready))))
    }
}
