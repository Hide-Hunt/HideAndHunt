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

    private fun onGame(id: Int) = onView(allOf(
            isDescendantOfA(withId(R.id.replay_info_list_recycler)),
            withId(R.id.constraintLayout),
            hasDescendant(allOf(withId(R.id.gameName), withText("Game #$id")))
    ))

    @Test
    fun correctDisplay(){
        onView(withId(R.id.replay_info_list_recycler)).check(matches(hasChildCount(6)))

        onGame(0).check(matches(hasSibling(withDrawable(R.drawable.ic_saved))))
        onGame(1).check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
        onGame(2).check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
        onGame(3).check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
        onGame(4).check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
        onGame(5).check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
    }

    @Test
    fun downloadSucceeded(){
        onGame(1).perform(click())

        onView(withText("Download")).perform(click())

        Thread.sleep(100)

        onGame(1).check(matches(hasSibling(withDrawable(R.drawable.ic_saved))))
    }

    @Test
    fun downloadFailed(){
        onGame(2).perform(click())

        onView(withText("Download")).perform(click())

        onGame(2).check(matches(hasSibling(withDrawable(R.drawable.ic_download))))
    }
}