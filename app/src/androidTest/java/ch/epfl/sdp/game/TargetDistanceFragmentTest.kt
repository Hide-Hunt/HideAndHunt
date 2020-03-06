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
        scenario.onFragment { fragment ->
            fragment.distance = 0
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "0 - 10")
            fragment.distance = 1
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "0 - 10")
            fragment.distance = 9
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "0 - 10")
            fragment.distance = 10
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "10 - 20")
            fragment.distance = 11
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "10 - 20")
            fragment.distance = 19
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "10 - 20")
            fragment.distance = 20
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "20 - 30")
            fragment.distance = 21
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "20 - 30")
            fragment.distance = 29
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "20 - 30")
            fragment.distance = 30
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "30 - 40")
            fragment.distance = 31
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "30 - 40")
            fragment.distance = 39
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "30 - 40")
            fragment.distance = 40
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "40+")
            fragment.distance = 41
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "40+")
            fragment.distance = 100
        }
        scenario.onFragment { fragment ->
            assertEquals(fragment.distanceLabel.text, "40+")
            fragment.distance = TargetDistanceFragment.NO_DISTANCE
        }
        scenario.onFragment { fragment ->
            val resources = ApplicationProvider.getApplicationContext<Application>().resources
            val text = resources.getString(R.string.no_distance_label)
            assertEquals(fragment.distanceLabel.text, text)
        }
    }

    @Test
    fun changingDistanceShouldDisplayCorrespondingImage() {
        val fragmentArgs = Bundle().apply {
            putSerializable("ranges", arrayListOf(0,10,20,30,40))
        }

        val scenario = launchFragmentInContainer<TargetDistanceFragment>(fragmentArgs)
        scenario.onFragment { it.distance = 0 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_4)))
        scenario.onFragment { it.distance = 1 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_4)))
        scenario.onFragment { it.distance = 9 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_4)))

        scenario.onFragment { it.distance = 10 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_3)))
        scenario.onFragment { it.distance = 11 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_3)))
        scenario.onFragment { it.distance = 19 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_3)))

        scenario.onFragment { it.distance = 20 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_2)))
        scenario.onFragment { it.distance = 21 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_2)))
        scenario.onFragment { it.distance = 29 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_2)))

        scenario.onFragment { it.distance = 30 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_1)))
        scenario.onFragment { it.distance = 31 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_1)))
        scenario.onFragment { it.distance = 39 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_1)))

        scenario.onFragment { it.distance = 40 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_0)))
        scenario.onFragment { it.distance = 41 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_0)))
        scenario.onFragment { it.distance = 100 }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_0)))
        scenario.onFragment { it.distance = TargetDistanceFragment.NO_DISTANCE }
        onView(withId(R.id.distanceImage)).check(matches(withDrawable(R.drawable.ic_signal_0)))
    }
}