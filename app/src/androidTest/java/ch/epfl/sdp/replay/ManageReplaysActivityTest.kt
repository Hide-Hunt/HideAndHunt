package ch.epfl.sdp.replay

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.EspressoTestsMatchers.withDrawable
import ch.epfl.sdp.R
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ManageReplaysActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(ManageReplaysActivity::class.java, false, true)

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
        onView(withId(R.id.replay_info_list_recycler)).check(matches(hasChildCount(6)))

        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #0")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_saved))))

        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #1")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #2")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #3")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #4")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #5")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
    }

    @Test
    fun downloadSucceeded(){
        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #1")))
                .perform(click())

        onView(withText("Download")).perform(click())

        Thread.sleep(100)

        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #1")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_saved))))
    }

    @Test
    fun downloadFailed(){

        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #2")))
                .perform(click())
        onView(withText("Download")).perform(click())

        onView(allOf(isDescendantOfA(withId(R.id.replay_info_list_recycler)), withId(R.id.gameName), withText("Game #2")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
    }
}