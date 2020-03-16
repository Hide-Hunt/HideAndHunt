package ch.epfl.sdp.lobby.global

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.DefaultRepoFactory
import ch.epfl.sdp.R
import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.lobby.LobbyActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GlobalLobbyActivityTest {
    @get:Rule
    val activityRule = IntentsTestRule(GlobalLobbyActivity::class.java, false, false)

    lateinit var repoFactory: IRepoFactory
    @Before
    fun setup() {
        repoFactory = object : DefaultRepoFactory() {
            override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
                return MockGlobalLobbyRepository()
            }
        }
    }

    @Test
    fun activityDoesNotCrash() {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("repoFactory", repoFactory)
        val scenario = activityRule.launchActivity(intent)
        Thread.sleep(100)
        scenario.runOnUiThread { scenario.recreate() }
        Thread.sleep(100)
    }

}