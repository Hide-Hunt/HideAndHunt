package ch.epfl.sdp.replay

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.EspressoTestsMatchers.withDrawable
import ch.epfl.sdp.R
import org.hamcrest.Matchers
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
        Espresso.onView(ViewMatchers.withId(R.id.replay_info_list_recycler)).check(ViewAssertions.matches(ViewMatchers.hasChildCount(6)))

        Espresso.onView(Matchers.allOf(ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.replay_info_list_recycler)), ViewMatchers.withId(R.id.gameName), ViewMatchers.withText("Game #0")))
                .check(matches(ViewMatchers.hasSibling(withDrawable(R.drawable.ic_saved))))

        Espresso.onView(Matchers.allOf(ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.replay_info_list_recycler)), ViewMatchers.withId(R.id.gameName), ViewMatchers.withText("Game #1")))
                .check(matches(ViewMatchers.hasSibling(withDrawable(R.drawable.ic_download))))
        Espresso.onView(Matchers.allOf(ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.replay_info_list_recycler)), ViewMatchers.withId(R.id.gameName), ViewMatchers.withText("Game #2")))
                .check(matches(ViewMatchers.hasSibling(withDrawable(R.drawable.ic_download))))
        Espresso.onView(Matchers.allOf(ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.replay_info_list_recycler)), ViewMatchers.withId(R.id.gameName), ViewMatchers.withText("Game #3")))
                .check(matches(ViewMatchers.hasSibling(withDrawable(R.drawable.ic_download))))
        Espresso.onView(Matchers.allOf(ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.replay_info_list_recycler)), ViewMatchers.withId(R.id.gameName), ViewMatchers.withText("Game #4")))
                .check(matches(ViewMatchers.hasSibling(withDrawable(R.drawable.ic_download))))
        Espresso.onView(Matchers.allOf(ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.replay_info_list_recycler)), ViewMatchers.withId(R.id.gameName), ViewMatchers.withText("Game #5")))
                .check(matches(ViewMatchers.hasSibling(withDrawable(R.drawable.ic_download))))
    }

    @Test
    fun downloadLaunched(){

    }

    @Test
    fun downloadFailed(){

    }
}