package ch.epfl.sdp.game

import android.content.Intent
import android.widget.Toast
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import ch.epfl.sdp.EspressoTestsMatchers.withDrawable
import ch.epfl.sdp.NFCTestHelper
import ch.epfl.sdp.PassMissingRoot
import ch.epfl.sdp.R
import ch.epfl.sdp.ToastMatcher
import ch.epfl.sdp.game.NFCTagHelper.byteArrayFromHexString
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test

class PredatorActivityTest {
    private val players = arrayListOf(
            Predator(0),
            Prey(1, "AAAA"),
            Predator(2),
            Prey(3, "BBBB"),
            Predator(4),
            Prey(5, "CCCC")
    )

    @get:Rule
    val activityRule = ActivityTestRule(PredatorActivity::class.java, false, false)

    @Test
    fun activityWithoutStartIntentDoesntCrash() {
        launchActivity<PredatorActivity>()
    }

    @Test
    fun activityWithStartIntentDoesntCrash() {
        val activityIntent = Intent()
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("gameID", 0)
        activityIntent.putExtra("playerID", 0)
        activityIntent.putExtra("players", players)
        val activity = activityRule.launchActivity(activityIntent)
        activityRule.runOnUiThread {
            activity.recreate()
        }
    }

    @Test
    fun allPreyShouldBeInitiallyAlive() {
        val activityIntent = Intent()
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("gameID", 0)
        activityIntent.putExtra("playerID", 0)
        activityIntent.putExtra("players", players)

        activityRule.launchActivity(activityIntent)

        onView(withId(R.id.preyStateList)).check(matches(hasChildCount(3)))

        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 1")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 3")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 5")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
    }

    @Test
    fun killingAPreyShouldLeaveAllOtherAliveAndTheKilledPreyDead() {
        val activityIntent = Intent()
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("gameID", 0)
        activityIntent.putExtra("playerID", 0)
        activityIntent.putExtra("players", players)

        activityRule.launchActivity(activityIntent)

        val tag = NFCTestHelper.createMockTag((players[3] as Prey).NFCTag.byteArrayFromHexString())
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)

        activityRule.runOnUiThread {
            activityRule.activity.onNewIntent(nfcIntent)
        }

        onView(withId(R.id.preyStateList)).check(matches(hasChildCount(3)))

        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 1")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 3")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_skull_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 5")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
    }

    @Test
    fun scanningEmptyTagShouldNotTriggerCallback() {
        val activityIntent = Intent()
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("gameID", 0)
        activityIntent.putExtra("playerID", 0)
        activityIntent.putExtra("players", players)

        activityRule.launchActivity(activityIntent)

        val tag = NFCTestHelper.createMockTag(ByteArray(0))
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)

        activityRule.runOnUiThread {
            activityRule.activity.onNewIntent(nfcIntent)
        }

        onView(withId(R.id.preyStateList)).check(matches(hasChildCount(3)))

        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 1")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 3")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 5")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
    }

    @Test
    fun scanningWrongTagShouldNotTriggerCallback() {
        val activityIntent = Intent()
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("gameID", 0)
        activityIntent.putExtra("playerID", 0)
        activityIntent.putExtra("players", players)


        activityRule.launchActivity(activityIntent)

        val tag = NFCTestHelper.createMockTag("ABBA".byteArrayFromHexString())
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)

        activityRule.runOnUiThread {
            activityRule.activity.onNewIntent(nfcIntent)
        }

        onView(withId(R.id.preyStateList)).check(matches(hasChildCount(3)))

        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 1")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 3")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 5")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
    }
}