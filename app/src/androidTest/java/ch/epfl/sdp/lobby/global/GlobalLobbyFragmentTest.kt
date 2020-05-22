package ch.epfl.sdp.lobby.global

import android.os.Bundle
import androidx.core.view.size
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.lobby.game.GameLobbyActivity
import org.junit.*
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class GlobalLobbyFragmentTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(GlobalLobbyActivity::class.java, false, false)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun recyclerViewIsVisible() {
        launchFragmentInContainer<GlobalLobbyFragment>()
        onView(withId(R.id.global_lobby_recycler)).check(matches(isDisplayed()))
    }

    @Test
    fun recyclerViewContainsItems() {
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>()

        var size = -1
        scenario.onFragment { f-> size = f.view!!.findViewById<RecyclerView>(R.id.global_lobby_recycler).size}
        println(size)
        Assert.assertTrue(size > 0)
    }

    @Test
    fun swipeRefreshesDataSet() {
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>()
        var size = -1
        scenario.onFragment { f-> size = f.view!!.findViewById<RecyclerView>(R.id.global_lobby_recycler).size}
        Assert.assertEquals(3, size)

        val recyclerView: ViewInteraction = onView(withId(R.id.global_lobby_recycler))
        recyclerView.perform(swipeDown())

        scenario.onFragment { f-> size = f.view!!.findViewById<RecyclerView>(R.id.global_lobby_recycler).size}
        Assert.assertEquals(4, size)
    }

    @Test
    fun clickOnGameStartsLobbyActivity() {
        launchFragmentInContainer<GlobalLobbyFragment>()
        val recyclerView: ViewInteraction = onView(withId(R.id.global_lobby_recycler))
        recyclerView.perform(actionOnItemAtPosition<GlobalLobbyAdapter.MyViewHolder>(0, click()))
        intended(IntentMatchers.hasComponent(GameLobbyActivity::class.java.name))
    }
}