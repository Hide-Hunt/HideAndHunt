package ch.epfl.sdp.replay

import androidx.test.espresso.intent.Intents
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ManageReplaysActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(ManageReplaysActivity::class.java, false, false)

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun correctDisplay(){

    }

    @Test
    fun downloadLaunched(){

    }

    @Test
    fun downloadFailed(){

    }
}