package ch.epfl.sdp.lobby.global

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import ch.epfl.sdp.lobby.LobbyActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GlobalLobbyActivityTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule(GlobalLobbyActivity::class.java)

    @Test
    fun activityDoesNotCrash() {
        launchActivity<GlobalLobbyActivity>()
        Thread.sleep(100)
    }

}