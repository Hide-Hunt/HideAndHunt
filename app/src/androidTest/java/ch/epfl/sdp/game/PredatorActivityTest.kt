package ch.epfl.sdp.game

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.EspressoTestsMatchers.withDrawable
import ch.epfl.sdp.NFCTestHelper
import ch.epfl.sdp.R
import ch.epfl.sdp.game.NFCTagHelper.byteArrayFromHexString
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test


class PredatorActivityTest {
    private val players = arrayListOf(
            Predator("0"),
            Prey("1", "AAAA"),
            Predator("2"),
            Prey("3", "BBBB"),
            Predator("4"),
            Prey("5", "CCCC")
    )

    private val activityIntent = Intent()
    init {
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("gameID", 0)
        activityIntent.putExtra("playerID", 0)
        activityIntent.putExtra("players", players)
        activityIntent.putExtra("initialTime", 2 * 60 * 1000L)
        activityIntent.putExtra("mqttURI", "tcp://localhost:1883")
    }

    @get:Rule
    val activityRule = ActivityTestRule(PredatorActivity::class.java, false, false)
    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)
    @get:Rule
    var grantPermissionRule2: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Test
    fun activityWithoutStartIntentDoesntCrash() {
        launchActivity<PredatorActivity>()
    }

    @Test
    fun activityWithStartIntentDoesntCrash() {
        val activity = activityRule.launchActivity(activityIntent)
        activityRule.runOnUiThread {
            activity.recreate()
        }
    }

    @Test
    fun allPreyShouldBeInitiallyAlive() {
        activityRule.launchActivity(activityIntent)

        onView(withId(R.id.preyStateList)).check(matches(hasChildCount(3)))

        checkAllPreyAlive()
    }

    @Test
    fun killingAPreyShouldLeaveAllOtherAliveAndTheKilledPreyDead() {
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
        activityRule.launchActivity(activityIntent)

        val tag = NFCTestHelper.createMockTag(ByteArray(0))
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)

        activityRule.runOnUiThread {
            activityRule.activity.onNewIntent(nfcIntent)
        }

        onView(withId(R.id.preyStateList)).check(matches(hasChildCount(3)))

        checkAllPreyAlive()
    }

    @Test
    fun scanningWrongTagShouldNotTriggerCallback() {
        activityRule.launchActivity(activityIntent)

        val tag = NFCTestHelper.createMockTag("ABBA".byteArrayFromHexString())
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)

        activityRule.runOnUiThread {
            activityRule.activity.onNewIntent(nfcIntent)
        }

        onView(withId(R.id.preyStateList)).check(matches(hasChildCount(3)))

        checkAllPreyAlive()
    }

    private fun checkAllPreyAlive() {
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 1")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 3")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
        onView(allOf(isDescendantOfA(withId(R.id.preyStateList)), withId(R.id.prey_name), withText("Prey 5")))
                .check(matches(hasSibling(withDrawable(R.drawable.ic_running_icon))))
    }
}