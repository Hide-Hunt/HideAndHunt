package ch.epfl.sdp.lobby.global

import androidx.core.view.size
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.PreyActivity
import ch.epfl.sdp.R
import ch.epfl.sdp.db.MockDB
import ch.epfl.sdp.game.GameTimerFragment
import ch.epfl.sdp.lobby.LobbyActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GlobalLobbyFragmentTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(GlobalLobbyActivity::class.java)

    @Test
    fun fragmentDoesNotCrash() {
        launchFragmentInContainer<GlobalLobbyFragment>()
    }

    @Test
    fun recyclerViewIsVisible() {
        val frag = GlobalLobbyFragment()
        frag.repo = MockGlobalLobbyRepository(MockDB())
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>(null, R.style.FragmentScenarioEmptyFragmentActivityTheme) {  frag }
        onView(withId(R.id.global_lobby_recycler)).check(matches(isDisplayed()))
    }

    @Test
    fun recyclerViewContainsItems() {
        val frag = GlobalLobbyFragment()
        frag.repo = MockGlobalLobbyRepository(MockDB())
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>(null, R.style.FragmentScenarioEmptyFragmentActivityTheme) {  frag }

        var size = -1
        scenario.onFragment { f-> size = f.view!!.findViewById<RecyclerView>(R.id.global_lobby_recycler).size}
        println(size)
        Assert.assertTrue(size > 0)
    }

    @Test
    fun clickOnGameStartsLobbyActivity() {
        val frag = GlobalLobbyFragment()
        frag.repo = MockGlobalLobbyRepository(MockDB())
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>(null, R.style.FragmentScenarioEmptyFragmentActivityTheme) {  frag }
        onView(withId(R.id.global_lobby_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition<GlobalLobbyAdapter.MyViewHolder>(0, click()))
        Intents.intended(IntentMatchers.hasComponent(LobbyActivity::class.java.name))
    }
}