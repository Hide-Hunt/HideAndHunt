package ch.epfl.sdp.game

import android.content.Intent
import android.widget.Toast
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import ch.epfl.sdp.NFCTestHelper
import ch.epfl.sdp.PassMissingRoot
import ch.epfl.sdp.R
import ch.epfl.sdp.ToastMatcher
import ch.epfl.sdp.game.NFCTagHelper.byteArrayFromHexString
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import org.hamcrest.CoreMatchers.containsString
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
    fun scanningEmptyTagShouldNotTriggerCallback() {
        val activityIntent = Intent()
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("gameID", 0)
        activityIntent.putExtra("playerID", 0)
        activityIntent.putExtra("players", players)


        activityRule.launchActivity(activityIntent)

        val tag = NFCTestHelper.createMockTag((players[3] as Prey).NFCTag.byteArrayFromHexString())
        val nfcIntent = NFCTestHelper.createTechDiscovered(tag)

        Thread.sleep(Toast.LENGTH_LONG.toLong() * 2) // To make sure no toast is displayed
        activityRule.runOnUiThread {
            activityRule.activity.onNewIntent(nfcIntent)
        }

        Thread.sleep(100)

        //onView(withId(R.id.preyStateDisplay), withText("Prey 1"))
        onView(withText("Catched a prey : id=3")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
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

        Thread.sleep(Toast.LENGTH_LONG.toLong() * 2) // To make sure no toast is displayed
        activityRule.runOnUiThread {
            activityRule.activity.onNewIntent(nfcIntent)
        }

        onView(withText(containsString("Catched a prey"))).inRoot(ToastMatcher()).withFailureHandler(PassMissingRoot()).check(doesNotExist())
    }
}