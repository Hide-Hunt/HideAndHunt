package ch.epfl.sdp.game

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.EspressoTestsMatchers.withDrawable
import ch.epfl.sdp.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PredatorRadarFragmentTest {

    @Test
    fun closestPredatorDistanceIsCorrect() {
        val fragmentArgs = Bundle().apply { putSerializable("ranges", ArrayList(listOf(5,10,20,50,Integer.MAX_VALUE))) }
        val scenario = launchFragmentInContainer<PredatorRadarFragment>(fragmentArgs)
        val rangePop = HashMap<Int, Int>()
        rangePop[5] = 1
        rangePop[10] = 10
        var str = ""
        scenario.onFragment { fragment ->
            fragment.updateInfos(2f, rangePop)
            str = fragment.getString(R.string.closest_predator).format(2f)
        }
        onView(withId(R.id.txt_closest_predator)).check(matches(withText(str)))
    }

    @Test
    fun predatorsAroundAreCorrect() {
        val fragmentArgs = Bundle().apply { putSerializable("ranges", ArrayList(listOf(5,10,20,50,Integer.MAX_VALUE))) }
        val scenario = launchFragmentInContainer<PredatorRadarFragment>(fragmentArgs)
        val rangePop = HashMap<Int, Int>()
        rangePop[5] = 1
        rangePop[10] = 12
        var str = ""
        scenario.onFragment { fragment ->
            fragment.updateInfos(2f, rangePop)
            str = fragment.getString(R.string.predator_around).format(13, 10)
        }
        onView(withId(R.id.txt_predator_around)).check(matches(withText(str)))
    }

    @Test
    fun correctDistanceLogoIsDisplayed() {
        val fragmentArgs = Bundle().apply { putSerializable("ranges", ArrayList(listOf(5,10,20,50,Integer.MAX_VALUE))) }
        val scenario = launchFragmentInContainer<PredatorRadarFragment>(fragmentArgs)
        val rangePop = HashMap<Int, Int>()
        scenario.onFragment { fragment ->
            fragment.updateInfos(3f, rangePop)
        }
        onView(withId(R.id.img_predator_distance)).check(matches(withDrawable(R.drawable.ic_predator_distance_0)))
        scenario.onFragment { fragment ->
            fragment.updateInfos(7f, rangePop)
        }
        onView(withId(R.id.img_predator_distance)).check(matches(withDrawable(R.drawable.ic_predator_distance_1)))
        scenario.onFragment { fragment ->
            fragment.updateInfos(12f, rangePop)
        }
        onView(withId(R.id.img_predator_distance)).check(matches(withDrawable(R.drawable.ic_predator_distance_2)))
        scenario.onFragment { fragment ->
            fragment.updateInfos(49f, rangePop)
        }
        onView(withId(R.id.img_predator_distance)).check(matches(withDrawable(R.drawable.ic_predator_distance_3)))
    }
}