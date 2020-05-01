package ch.epfl.sdp.replay.viewer

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.R
import ch.epfl.sdp.game.PredatorActivity
import org.junit.After
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import java.io.File

class ReplayActivityTest {

    private val activityIntent = Intent()
    init {
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("replay_path", "0.game")
    }

    private val invalidPathActivityIntent = Intent()
    init {
        invalidPathActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        invalidPathActivityIntent.putExtra("replay_path", "inexisting")
    }

    private val emptyIntent = Intent()
    init {
        emptyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    @get:Rule
    val activityRule = ActivityTestRule(ReplayActivity::class.java, false, false)
    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @get:Rule
    var grantPermissionRule1: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun testNoFilePathProvided(){
        activityRule.launchActivity(emptyIntent)
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText(R.string.missing_replay_path_parameter)))
    }

    @Test
    fun testInvalidFilePathProvided(){
        activityRule.launchActivity(invalidPathActivityIntent)
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText("File not found /data/user/0/ch.epfl.sdp/files/replays/inexisting")))
    }

    @Test
    fun testValidFilePathProvided(){
        activityRule.launchActivity(activityIntent)
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText("")))
    }
}