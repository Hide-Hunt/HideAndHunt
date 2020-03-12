package ch.epfl.sdp.lobby

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class GameCreationActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(LobbyActivity::class.java)

    @Test
    fun testLaunchWithoutCrash() {
        launchActivity<GameCreationActivity>()
    }
}