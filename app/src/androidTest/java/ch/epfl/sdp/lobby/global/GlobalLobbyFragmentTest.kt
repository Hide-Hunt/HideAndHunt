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
import ch.epfl.sdp.DefaultMockRepoFactory
import ch.epfl.sdp.R
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.IRepoFactory
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

    private val repoFactory: IRepoFactory = object : DefaultMockRepoFactory() {
        override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
            return object : IGlobalLobbyRepository {

                private var callID = 0

                override fun getAllGames(cb: Callback<List<Game>>) {
                    if(callID == 0) {
                        cb(listOf(Game(0, "Blabla", "Bloublou", 10000, emptyMap(), GameState.LOBBY, listOf(), Date(), Date(), Date(), 0)))
                    } else {
                        cb(listOf(
                                Game(0, "Blabla", "Bloublou", 10000, emptyMap(), GameState.STARTED, listOf(), Date(), Date(), Date(), 0),
                                Game(1, "Jean", "Michel", 8000, emptyMap(), GameState.LOBBY, listOf(), Date(), Date(), Date(), 0)
                        ))
                    }
                    callID++
                }
            }
        }
    }

    private val baseBundle: Bundle = Bundle().apply { putSerializable("repoFacto", repoFactory) }

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
        launchFragmentInContainer<GlobalLobbyFragment>(baseBundle)
        onView(withId(R.id.global_lobby_recycler)).check(matches(isDisplayed()))
    }

    @Test
    fun recyclerViewContainsItems() {
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>(baseBundle)

        var size = -1
        scenario.onFragment { f-> size = f.view!!.findViewById<RecyclerView>(R.id.global_lobby_recycler).size}
        println(size)
        Assert.assertTrue(size > 0)
    }

    @Test
    fun swipeRefreshesDataset() {
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>(baseBundle)
        var size = -1
        scenario.onFragment { f-> size = f.view!!.findViewById<RecyclerView>(R.id.global_lobby_recycler).size}
        Assert.assertEquals(1, size)

        val recyclerView: ViewInteraction = onView(withId(R.id.global_lobby_recycler))
        recyclerView.perform(swipeDown())

        scenario.onFragment { f-> size = f.view!!.findViewById<RecyclerView>(R.id.global_lobby_recycler).size}
        Assert.assertEquals(2, size)
    }

    @Test
    fun clickOnGameStartsLobbyActivity() {
        launchFragmentInContainer<GlobalLobbyFragment>(baseBundle)
        val recyclerView: ViewInteraction = onView(withId(R.id.global_lobby_recycler))
        recyclerView.perform(actionOnItemAtPosition<GlobalLobbyAdapter.MyViewHolder>(0, click()))
        intended(IntentMatchers.hasComponent(GameLobbyActivity::class.java.name))
    }
}