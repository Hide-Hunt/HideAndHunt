package ch.epfl.sdp.game

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Prey
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TargetSelectionFragmentTest {
    private val preys = arrayListOf(
            Prey("1", "AAAA"),
            Prey("2", "BBBB"),
            Prey("3", "CCCC")
    )

    @Test
    fun settingListenerCallsOnTargetSelected() {
        val scenario = launchFragmentInContainer<TargetSelectionFragment>()
        var callbackCalled = false
        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            override fun onTargetSelected(targetID: String) {
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
            override fun onTargetSelected(targetID: String) {
                assertEquals(TargetSelectionFragment.NO_TARGET, targetID)
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }

        scenario.onFragment { fragment -> fragment.selectedTargetID = "0" }
        scenario.onFragment { fragment -> fragment.selectedTargetID = "1" }
        scenario.onFragment { fragment -> fragment.selectedTargetID = "2" }
        scenario.onFragment { fragment -> fragment.selectedTargetID = TargetSelectionFragment.NO_TARGET }
    }

    @Test
    fun listenerCallbackShouldBeCalledWithNoTargetWhenTargetChangesToInvalidId() {
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", preys)
        }
        val scenario = launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            override fun onTargetSelected(targetID: String) {
                assertEquals(TargetSelectionFragment.NO_TARGET, targetID)
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }

        scenario.onFragment { fragment -> fragment.selectedTargetID = "0" }
        scenario.onFragment { fragment -> fragment.selectedTargetID = "4" }
        scenario.onFragment { fragment -> fragment.selectedTargetID = "5" }
        scenario.onFragment { fragment -> fragment.selectedTargetID = TargetSelectionFragment.NO_TARGET }
    }

    @Test
    fun listenerCallbackShouldBeCalledWhenTargetChanges() {
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", preys)
        }
        val scenario = launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            var expectedTargetInCallback = "1"
            override fun onTargetSelected(targetID: String) {
                assertEquals(expectedTargetInCallback, targetID)
                var expectedTargetInCallbackInt = expectedTargetInCallback.toInt()
                expectedTargetInCallback = expectedTargetInCallbackInt++.toString()
                if (expectedTargetInCallback == "4") {
                    expectedTargetInCallback = TargetSelectionFragment.NO_TARGET
                }
            }
        }

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { fragment -> fragment.listener = listener }

        scenario.onFragment { fragment -> fragment.selectedTargetID = "1"}
        scenario.onFragment { fragment -> fragment.selectedTargetID = "2" }
        scenario.onFragment { fragment -> fragment.selectedTargetID = "3" }
        scenario.onFragment { fragment -> fragment.selectedTargetID = TargetSelectionFragment.NO_TARGET }
    }

    @Test
    fun clickingOnCrosshairIconShouldShowTargetSelectionDialog() {
        launchFragmentInContainer<TargetSelectionFragment>()
        // Click on the crosshair icon
        onView(withId(R.id.crosshair_icon)).perform(click())
        // Check for dialog being displayed
        onView(withText(R.string.target_selection_dialog_title)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun clickingTextShouldShowTargetSelectionDialog() {
        launchFragmentInContainer<TargetSelectionFragment>()
        // Click on the current target text
        onView(withId(R.id.currentTarget)).perform(click())
        // Check for dialog being displayed
        onView(withText(R.string.target_selection_dialog_title)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun clickingOnSelectionMainLayoutShouldShowTargetSelectionDialog() {
        launchFragmentInContainer<TargetSelectionFragment>()
        // Click on the current target text
        onView(withId(R.id.targetSelectionMainLayout)).perform(click())
        // Check for dialog being displayed
        onView(withText(R.string.target_selection_dialog_title)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun targetSelectionDialogShouldListAllTargets() {
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", preys)
        }
        launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        for (prey in preys) {
            onView(withText(prey.toString())).check(doesNotExist())
        }

        onView(withId(R.id.targetSelectionMainLayout)).perform(click())

        for (prey in preys) {
            onView(withText(prey.toString())).inRoot(isDialog()).check(matches(isDisplayed()))
        }
    }

    @Test
    fun selectingTargetShouldCallListenerWithTargetID() {
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", preys)
        }
        val scenario = launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        val listener = object : TargetSelectionFragment.OnTargetSelectedListener {
            var expectedTargetInCallback = 1
            override fun onTargetSelected(targetID: String) {
                assertEquals(expectedTargetInCallback, targetID)
                Log.d("tested", "assertEquals(%s, %s)".format(expectedTargetInCallback, targetID))
                expectedTargetInCallback++
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }

        for (prey in preys) {
            onView(withId(R.id.targetSelectionMainLayout)).perform(click())
            onView(withText(prey.toString())).inRoot(isDialog()).perform(click())
        }
    }

    @Test
    fun selectingTargetShouldUpdateTargetText() {
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", preys)
        }
        launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("No target")))
        for (prey in preys) {
            onView(withId(R.id.targetSelectionMainLayout)).perform(click())
            onView(withText(prey.toString())).inRoot(isDialog()).perform(click())
            onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("Player " + prey.id)))
        }
    }

    @Test
    fun selectionShouldStayAfterRecreation() {
        val fragmentArgs = Bundle().apply {
            putSerializable("targets", preys)
        }

        val scenario = launchFragmentInContainer<TargetSelectionFragment>(fragmentArgs)

        onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("No target")))
        onView(withId(R.id.targetSelectionMainLayout)).perform(click())
        onView(withText(preys[1].toString())).inRoot(isDialog()).perform(click())
        onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("Player 2")))

        scenario.recreate()

        onView(withId(R.id.currentTarget)).check(matches(isDisplayed())).check(matches(withText("Player 2")))
    }
}