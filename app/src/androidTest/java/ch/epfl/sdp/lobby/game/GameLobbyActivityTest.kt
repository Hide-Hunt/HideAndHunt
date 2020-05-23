package ch.epfl.sdp.lobby.game

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.EspressoViewAssertions.RecyclerViewItemCount
import ch.epfl.sdp.NFCTestHelper
import ch.epfl.sdp.R
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.game.NFCTagHelper.byteArrayFromHexString
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import kotlinx.android.synthetic.main.activity_game_lobby.*
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameLobbyActivityTest {
    private lateinit var activityIntent: Intent

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        activityIntent = Intent(ctx, GameLobbyActivity::class.java)
        activityIntent.putExtra("gameID", "g4m31d")
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        LocalUser.uid = "u53r1d"
    }

    @Test
    fun activityDoesNotCrash() {
        launchActivity<GameLobbyActivity>(activityIntent)
        onView(withId(R.id.swipe_container)).perform(swipeDown())
    }

    @Test
    fun swipeDownAndFactionChangeRefreshesData() {
        val scenario = launchActivity<GameLobbyActivity>(activityIntent)

        // Initially 11 players
        onView(withId(R.id.player_list)).check(RecyclerViewItemCount(11))

        // Add one then change faction => should reload
        scenario.onActivity {
            (it.repository as MockGameLobbyRepository).players
                    .add(Participation("Player11", Faction.PREY, false, "0ABC", ""))
        }
        onView(withId(R.id.faction_selection)).perform(click())
        onView(withId(R.id.player_list)).check(RecyclerViewItemCount(12))

        // Add one then swipe => should reload again
        scenario.onActivity {
            (it.repository as MockGameLobbyRepository).players
                    .add(Participation("Player12", Faction.PREY, false, "0ABC", ""))
        }
        onView(withId(R.id.player_list)).perform(swipeDown())
        onView(withId(R.id.player_list)).check(RecyclerViewItemCount(13))
    }

    @Test
    fun scanningTagShouldChangeReadyState() {
        val scenario = launchActivity<GameLobbyActivity>(activityIntent)

        scenario.onActivity {
            it.onFactionChange(Faction.PREY)
        }

        onView(withId(R.id.txt_player_ready)).check(matches(withText("You are not ready, scan your NFC tag")))

        val tag = NFCTestHelper.createMockTag("ABBA".byteArrayFromHexString())
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)
        scenario.onActivity {
            it.onNewIntent(nfcIntent)
        }

        onView(withId(R.id.txt_player_ready)).check(matches(withText("You are ready")))
    }

    @Test
    fun startButtonShouldBeDisabledAfterClickingOnIt() {
        val scenario = launchActivity<GameLobbyActivity>(activityIntent)

        onView(withId(R.id.start_button)).check(matches(isEnabled()))
        onView(withId(R.id.start_button)).perform(click())
        onView(withId(R.id.start_button)).check(matches(not(isEnabled())))

        // In the mock repo, a started game cannot be restarted, leading to an error
        // In case of starting error, the button should be re-enabled
        scenario.onActivity {
            it.start_button.isEnabled = true
        }

        onView(withId(R.id.start_button)).check(matches(isEnabled()))
        onView(withId(R.id.start_button)).perform(click())
        onView(withId(R.id.start_button)).check(matches(isEnabled()))
    }
}
