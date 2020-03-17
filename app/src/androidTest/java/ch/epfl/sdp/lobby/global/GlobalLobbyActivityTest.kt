package ch.epfl.sdp.lobby.global

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.DefaultMockRepoFactory
import ch.epfl.sdp.db.IRepoFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GlobalLobbyActivityTest {
    @get:Rule
    val activityRule = IntentsTestRule(GlobalLobbyActivity::class.java, false, false)

    private lateinit var repoFactory: IRepoFactory
    @Before
    fun setup() {
        repoFactory = object : DefaultMockRepoFactory() {
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