package ch.epfl.sdp.game

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TargetSelectionFragmentTest {
    @Test
    fun settingListenerCallsOnTargetSelected() {
        val scenario = launchFragmentInContainer<TargetSelectionFragment>()
        var callbackCalled = false
        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            override fun onTargetSelected(targetID: Int) {
                callbackCalled = true
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }
        assert(callbackCalled)
    }

    @Test
    fun defaultTargetShouldBeNoTarget() {
        val scenario = launchFragmentInContainer<TargetSelectionFragment>()
        scenario.onFragment { fragment ->
            assertEquals(fragment.selectedTargetID, TargetSelectionFragment.NO_TARGET)
        }
    }

    @Test
    fun listenerCallbackShouldBeCalledWithNoTargetWhenTargetChangesWithEmptyTargetList() {
        val scenario = launchFragmentInContainer<TargetSelectionFragment>()

        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            override fun onTargetSelected(targetID: Int) {
                assertEquals(TargetSelectionFragment.NO_TARGET, targetID)
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }

        scenario.onFragment { fragment -> fragment.selectedTargetID = 0 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = 1 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = 2 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = TargetSelectionFragment.NO_TARGET }
    }

    @Test
    fun listenerCallbackShouldBeCalledWithNoTargetWhenTargetChangesToInvalidId() {
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", arrayListOf(
                    Player(3, Faction.PREDATOR),
                    Player(4, Faction.PREDATOR),
                    Player(5, Faction.PREDATOR)
            ))
        }
        val scenario = launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            override fun onTargetSelected(targetID: Int) {
                assertEquals(TargetSelectionFragment.NO_TARGET, targetID)
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }

        scenario.onFragment { fragment -> fragment.selectedTargetID = 0 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = 1 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = 2 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = TargetSelectionFragment.NO_TARGET }
    }

    @Test
    fun listenerCallbackShouldBeCalledWhenTargetChanges() {
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", arrayListOf(
                    Player(0, Faction.PREDATOR),
                    Player(1, Faction.PREDATOR),
                    Player(2, Faction.PREDATOR)
            ))
        }
        val scenario = launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            var expectedTargetInCallback = 0
            override fun onTargetSelected(targetID: Int) {
                assertEquals(expectedTargetInCallback, targetID)
                expectedTargetInCallback++
                if (expectedTargetInCallback == 3) {
                    expectedTargetInCallback = TargetSelectionFragment.NO_TARGET
                }
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }

        scenario.onFragment { fragment -> fragment.selectedTargetID = 0 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = 1 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = 2 }
        scenario.onFragment { fragment -> fragment.selectedTargetID = TargetSelectionFragment.NO_TARGET }
    }

    @Test
    fun clickingOnCrosshairIconShouldShowTargetSelectionDialog() {
        launchFragmentInContainer<TargetSelectionFragment>()
        // Click on the crosshair icon
        onView(withId(R.id.crosshair_icon)).perform(click())
        // Check for dialog being displayed
        onView(withText(R.string.targetSelectionDialogTitle)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun clickingTextShouldShowTargetSelectionDialog() {
        launchFragmentInContainer<TargetSelectionFragment>()
        // Click on the current target text
        onView(withId(R.id.currentTarget)).perform(click())
        // Check for dialog being displayed
        onView(withText(R.string.targetSelectionDialogTitle)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun clickingOnSelectionMainLayoutShouldShowTargetSelectionDialog() {
        launchFragmentInContainer<TargetSelectionFragment>()
        // Click on the current target text
        onView(withId(R.id.targetSelectionMainLayout)).perform(click())
        // Check for dialog being displayed
        onView(withText(R.string.targetSelectionDialogTitle)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun targetSelectionDialogShouldListAllTargets() {
        val targets = arrayListOf(
                Player(0, Faction.PREDATOR),
                Player(1, Faction.PREDATOR),
                Player(2, Faction.PREDATOR)
        )
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", targets)
        }
        launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        for (t in targets) {
            onView(withText(t.toString())).check(doesNotExist())
        }

        onView(withId(R.id.targetSelectionMainLayout)).perform(click())

        for (t in targets) {
            onView(withText(t.toString())).inRoot(isDialog()).check(matches(isDisplayed()))
        }
    }

    @Test
    fun selectingTargetShouldCallListenerWithTargetID() {
        val targets = arrayListOf(
                Player(0, Faction.PREDATOR),
                Player(1, Faction.PREDATOR),
                Player(2, Faction.PREDATOR)
        )
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", targets)
        }
        val scenario = launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            var expectedTargetInCallback = 0
            override fun onTargetSelected(targetID: Int) {
                assertEquals(expectedTargetInCallback, targetID)
                Log.d("tested", "assertEquals(%d, %d)".format(expectedTargetInCallback, targetID))
                expectedTargetInCallback++
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }

        for (t in targets) {
            onView(withId(R.id.targetSelectionMainLayout)).perform(click())
            onView(withText(t.toString())).inRoot(isDialog()).perform(click())
        }
    }

    @Test
    fun selectingTargetShouldUpdateTargetText() {
        val targets = arrayListOf(
                Player(0, Faction.PREDATOR),
                Player(1, Faction.PREDATOR),
                Player(2, Faction.PREDATOR)
        )
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", targets)
        }
        launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("No target")))
        for (t in targets) {
            onView(withId(R.id.targetSelectionMainLayout)).perform(click())
            onView(withText(t.toString())).inRoot(isDialog()).perform(click())
            onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("Player " + t.id)))
        }
    }

    @Test
    fun selectionShouldStayAfterRecreation() {
        val targets = arrayListOf(
                Player(0, Faction.PREDATOR),
                Player(1, Faction.PREDATOR),
                Player(2, Faction.PREDATOR)
        )
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", targets)
        }

        val scenario = launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("No target")))
        onView(withId(R.id.targetSelectionMainLayout)).perform(click())
        onView(withText(targets[1].toString())).inRoot(isDialog()).perform(click())
        onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("Player 1")))

        scenario.recreate()

        onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("Player 1")))
    }
}