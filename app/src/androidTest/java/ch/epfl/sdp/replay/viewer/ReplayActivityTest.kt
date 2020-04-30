package ch.epfl.sdp.replay.viewer

import android.content.Intent
import android.icu.text.CaseMap
import androidx.test.core.app.launchActivity
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

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class ReplayActivityTest {

    private val activityIntent = Intent()
    init {
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("replay_path", "test_file1.txt")
    }

    private val invalidPathActivityIntent = Intent()
    init {
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("replay_path", "inexisting")
    }

    @get:Rule
    val activityRule = ActivityTestRule(PredatorActivity::class.java, false, false)
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
        launchActivity<ReplayActivity>()
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText(R.string.missing_replay_path_parameter)))
    }

    @Test
    fun testInvalidFilePathProvided(){
        launchActivity<ReplayActivity>(invalidPathActivityIntent)
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText(R.string.file_not_found)))
    }

    @Test
    fun testValidFilePathProvided(){
        launchActivity<ReplayActivity>(activityIntent)
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText("")))
    }
}