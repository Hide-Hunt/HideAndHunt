package ch.epfl.sdp.game

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.EspressoTestsMatchers.withDrawable
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


class PreyActivityTest {
    private val players = arrayListOf(
            Predator(0),
            Prey(1, "AAAA"),
            Predator(2),
            Prey(3, "BBBB"),
            Predator(4),
            Prey(5, "CCCC")
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
    val activityRule = ActivityTestRule(PreyActivity::class.java, false, false)
    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)
    @get:Rule
    var grantPermissionRule2: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Test
    fun activityWithoutStartIntentDoesntCrash() {
        launchActivity<PreyActivity>()
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
    fun killingPreyShouldBeDisplayedInList() {
        val activity = activityRule.launchActivity(activityIntent)

        activity.runOnUiThread {
            activity.onPreyCatches(0, 3)
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
    fun changingOwnLocationCausesRecomputationOfDistance() {
        val activity = activityRule.launchActivity(activityIntent)
        activity.locationHandler.lastKnownLocation.latitude = 42.0
        activity.locationHandler.lastKnownLocation.longitude = 6.0
        activity.runOnUiThread {
            activity.players[0]!!.lastKnownLocation = Location(42.00002, 6.0) //2.2214656 meters away
            activity.onLocationChanged(activity.locationHandler.lastKnownLocation)
            assertEquals(1, activity.rangePopulation[activity.ranges[0]])
            activity.players[0]!!.lastKnownLocation = Location(43.0, 6.0) //2.2214656 meters away
            activity.onLocationChanged(activity.locationHandler.lastKnownLocation)
            assertEquals(0, activity.rangePopulation[activity.ranges[0]])
        }
    }

    @Test
    fun changingPredatorLocationCausesRecomputationOfDistance() {
        val activity = activityRule.launchActivity(activityIntent)
        activity.locationHandler.lastKnownLocation.latitude = 42.0
        activity.locationHandler.lastKnownLocation.longitude = 6.0
        activity.runOnUiThread {
            activity.onPlayerLocationUpdate(0, Location(42.00002, 6.0)) //2.2214656 meters away
            assertEquals(1, activity.rangePopulation[activity.ranges[0]])
            activity.onPlayerLocationUpdate(0, Location(43.0, 6.0)) //many kilometers away
            assertEquals(0, activity.rangePopulation[activity.ranges[0]])
        }
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