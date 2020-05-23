package ch.epfl.sdp.lobby.game

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.EspressoViewAssertions.RecyclerViewItemCount
import ch.epfl.sdp.NFCTestHelper
import ch.epfl.sdp.R
import ch.epfl.sdp.authentication.LocalUser
import ch.epfl.sdp.db.SuccFailCallbacks
import ch.epfl.sdp.game.NFCTagHelper.byteArrayFromHexString
import ch.epfl.sdp.game.PredatorActivity
import ch.epfl.sdp.game.PreyActivity
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import kotlinx.android.synthetic.main.activity_game_lobby.*
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameLobbyActivityTest {
    private lateinit var activityIntent: Intent

    @get:Rule
    val intentsTestRule = IntentsTestRule(GameLobbyActivity::class.java, false, false)

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        activityIntent = Intent(ctx, GameLobbyActivity::class.java)
        activityIntent.putExtra("gameID", "g4m31d")
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        LocalUser.uid = "u53r1d"
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun activityDoesNotCrash() {
        launchActivity<GameLobbyActivity>(activityIntent)
        onView(withId(R.id.swipe_container)).perform(swipeDown())
    }

    @Test
    fun swipeDownAndFactionChangeRefreshesData() {
        val scenario = launchActivity<GameLobbyActivity>(activityIntent)
        lateinit var repo: MockGameLobbyRepository
        scenario.onActivity {
            repo = it.repository as MockGameLobbyRepository
        }

        // Initially 11 players
        onView(withId(R.id.player_list)).check(RecyclerViewItemCount(11))

        // Add one then change faction => should reload
        scenario.onActivity {
            repo.players.add(Participation("Player11", Faction.PREY, false, "0ABC", ""))
        }
        onView(withId(R.id.faction_selection)).perform(click())
        onView(withId(R.id.player_list)).check(RecyclerViewItemCount(12))

        // Add one then swipe => should reload again
        scenario.onActivity {
            repo.players.add(Participation("Player12", Faction.PREY, false, "0ABC", ""))
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

    @Test
    fun onGameStartShouldLaunchThePreyActivityIfThePlayerIsAPrey() {
        val scenario = launchActivity<GameLobbyActivity>(activityIntent)
        // Test for prey
        scenario.onActivity {
            // Set player as a prey and scan a tag to make it ready
            it.onFactionChange(Faction.PREY)
            val tag = NFCTestHelper.createMockTag("ABBA".byteArrayFromHexString())
            val nfcIntent = NFCTestHelper.createTechDiscovered(tag)
            it.onNewIntent(nfcIntent)
            it.onGameStart()
        }
        Intents.intended(hasComponent(PreyActivity::class.java.name))
    }

    @Test
    fun onGameStartShouldLaunchThePredatorActivityIfThePlayerIsAPredator() {
        val scenario = launchActivity<GameLobbyActivity>(activityIntent)
        // Test for predator
        scenario.onActivity {
            it.onFactionChange(Faction.PREDATOR)
            it.onGameStart()
        }
        Intents.intended(hasComponent(PredatorActivity::class.java.name))
    }

    @Test
    fun onGameStartShouldLaunchGameActivityWithGameRelatedExtras() {
        val scenario = launchActivity<GameLobbyActivity>(activityIntent)
        lateinit var repo: MockGameLobbyRepository
        scenario.onActivity {
            repo = it.repository as MockGameLobbyRepository
            it.onGameStart()
        }

        // Starts PreyActivity or PredatorActivity
        Intents.intended(anyOf(hasComponent(PreyActivity::class.java.name), hasComponent(PredatorActivity::class.java.name)))
        // Has correct extras
        Intents.intended(allOf(
                hasExtra("initialTime", repo.gameDuration*1000L),
                hasExtra("gameID", "g4m31d"),
                hasExtra("playerID", 3)))
        repo.getPlayers("g4m31d", SuccFailCallbacks.SuccFailCallback({
            Intents.intended(hasExtra("players", it))
        }))
    }
}
