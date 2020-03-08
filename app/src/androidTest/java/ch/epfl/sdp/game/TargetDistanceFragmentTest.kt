package ch.epfl.sdp.game

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.EspressoTestsMatchers.noDrawable
import ch.epfl.sdp.EspressoTestsMatchers.withDrawable
import ch.epfl.sdp.R
import kotlinx.android.synthetic.main.fragment_target_distance.*
import org.junit.Assert.assertEquals
import org.junit.Test


class TargetDistanceFragmentTest {
    @Test
    fun defaultDistanceShouldBeDisabled() {
        val scenario = launchFragmentInContainer<TargetDistanceFragment>()
        scenario.onFragment { fragment ->
            assertEquals(fragment.distance, TargetDistanceFragment.DISABLED)
        }
    }

    @Test
    fun defaultLabelShouldBeTrackingDisabled() {
        val scenario = launchFragmentInContainer<TargetDistanceFragment>()
        scenario.onFragment { fragment ->
            val resources = ApplicationProvider.getApplicationContext<Application>().resources
            val text = resources.getString(R.string.tracking_disabled)
            assertEquals(fragment.distanceLabel.text, text)
        }
    }

    @Test
    fun defaultImageShouldBeNoSignalAnimation() {
        launchFragmentInContainer<TargetDistanceFragment>()
        onView(withId(R.id.distanceImage)).check(matches(noDrawable()))
    }

    @Test
    fun changingDistanceShouldDisplayCorrespondingRange() {
        val fragmentArgs = Bundle().apply {
            putSerializable("ranges", arrayListOf(0,10,20,30,40))
        }

        val scenario = launchFragmentInContainer<TargetDistanceFragment>(fragmentArgs)
        val resources = ApplicationProvider.getApplicationContext<Application>().resources
        val tests = listOf(
                Pair( 0,  "0 - 10"),    Pair( 1,  "0 - 10"),    Pair( 9,  "0 - 10"),
                Pair(10, "10 - 20"),    Pair(11, "10 - 20"),    Pair(19, "10 - 20"),
                Pair(20, "20 - 30"),    Pair(21, "20 - 30"),    Pair(29, "20 - 30"),
                Pair(30, "30 - 40"),    Pair(31, "30 - 40"),    Pair(39, "30 - 40"),
                Pair(40, "40+"),        Pair(41, "40+"),        Pair(100,"40+"),

                Pair(TargetDistanceFragment.NO_DISTANCE, resources.getString(R.string.no_distance_label))
        )

        for (test in tests) {
            scenario.onFragment { fragment ->
                fragment.distance = test.first
            }
            scenario.onFragment { fragment ->
                assertEquals(fragment.distanceLabel.text, test.second)
            }
        }
    }

    @Test
    fun changingDistanceShouldDisplayCorrespondingImage() {
        val fragmentArgs = Bundle().apply {
            putSerializable("ranges", arrayListOf(0,10,20,30,40))
        }

        val scenario = launchFragmentInContainer<TargetDistanceFragment>(fragmentArgs)
        val tests = listOf(
                Pair( 0, R.drawable.ic_signal_4),    Pair( 1, R.drawable.ic_signal_4),    Pair( 9, R.drawable.ic_signal_4),
                Pair(10, R.drawable.ic_signal_3),    Pair(11, R.drawable.ic_signal_3),    Pair(19, R.drawable.ic_signal_3),
                Pair(20, R.drawable.ic_signal_2),    Pair(21, R.drawable.ic_signal_2),    Pair(29, R.drawable.ic_signal_2),
                Pair(30, R.drawable.ic_signal_1),    Pair(31, R.drawable.ic_signal_1),    Pair(39, R.drawable.ic_signal_1),
                Pair(40, R.drawable.ic_signal_0),    Pair(41, R.drawable.ic_signal_0),    Pair(100,R.drawable.ic_signal_0),

                Pair(TargetDistanceFragment.NO_DISTANCE, R.drawable.ic_signal_0)
        )

        for (test in tests) {
            scenario.onFragment { fragment ->
                fragment.distance = test.first
            }
            onView(withId(R.id.distanceImage)).check(matches(withDrawable(test.second)))
        }
    }
}