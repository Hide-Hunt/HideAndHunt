package ch.epfl.sdp.lobby.global

import android.os.Bundle
import androidx.core.view.size
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.DefaultRepoFactory
import ch.epfl.sdp.R
import ch.epfl.sdp.db.Callback
import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.game.data.GameState
import ch.epfl.sdp.lobby.LobbyActivity
import org.hamcrest.core.AllOf.allOf
import org.junit.*
import org.junit.runner.RunWith
import java.io.Serializable
import java.util.Date

@RunWith(AndroidJUnit4::class)
class GlobalLobbyFragmentTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(GlobalLobbyActivity::class.java, false, false)

    val repoFactory: IRepoFactory = object : DefaultRepoFactory() {
        override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
            return object : IGlobalLobbyRepository {
                override fun getAllGames(cb: Callback<List<Game>>) {
                    cb(listOf(Game(0, "Blabla", "Bloublou", 10000, emptyMap(), GameState.LOBBY, listOf(), Date(), Date(), Date())))
                }
            }
        }
    }

    val baseBundle: Bundle = Bundle().apply { putSerializable("repoFacto", repoFactory) }

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
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>(baseBundle)
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
    fun clickOnGameStartsLobbyActivity() {
        val scenario = launchFragmentInContainer<GlobalLobbyFragment>(baseBundle)
        val recyclerView: ViewInteraction = onView(
                allOf(withId(R.id.global_lobby_recycler), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition<GlobalLobbyAdapter.MyViewHolder>(0, click()));
        intended(IntentMatchers.hasComponent(LobbyActivity::class.java.name))
    }
}