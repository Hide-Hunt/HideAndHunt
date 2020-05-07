package ch.epfl.sdp.replay.viewer

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.epfl.sdp.EspressoViewActions.clickSeekBar
import ch.epfl.sdp.R
import kotlinx.android.synthetic.main.fragment_replay_control.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DateFormat
import java.util.*
import kotlin.math.pow

class ReplayControlFragmentTest {
    data class DateTS(val timestamp: Long) {
        val systemDate: String = DateFormat.getDateTimeInstance().format(Date(timestamp * 1000))
    }

    private val firstDate = DateTS(1026211559) // 2002-07-09 10:45:59
    private val tenSeconds = DateTS(1026211569) // 2002-07-09 10:46:09
    private val after41m34s = DateTS(1026214053) // 2002-07-09 11:27:33

    private val tenSecondsGame = Bundle().apply {
        putInt(ReplayControlFragment.FIRST_TIMESTAMP, firstDate.timestamp.toInt())
        putInt(ReplayControlFragment.LAST_TIMESTAMP, tenSeconds.timestamp.toInt())
    }
    private val longGame = Bundle().apply {
        putInt(ReplayControlFragment.FIRST_TIMESTAMP, firstDate.timestamp.toInt())
        putInt(ReplayControlFragment.LAST_TIMESTAMP, after41m34s.timestamp.toInt())
    }

    @Test
    fun minAndMaxTimeSelectionCorrespondToGivenRange() {
        val scenario = launchFragmentInContainer<ReplayControlFragment>(longGame)
        onView(withId(R.id.speedFactor)).check(matches(withText("x0")))
        scenario.onFragment {
            assertEquals(0, it.timeSelectionBar.min)
            assertEquals(after41m34s.timestamp.toInt() - firstDate.timestamp.toInt(), it.timeSelectionBar.max)
        }
    }

    @Test
    fun initialSpeedShouldBeZero() {
        val scenario = launchFragmentInContainer<ReplayControlFragment>(tenSecondsGame)
        onView(withId(R.id.speedFactor)).check(matches(withText("x0")))
        scenario.onFragment {
            assertEquals(0, it.speedSelection.progress)
        }
    }

    @Test
    fun initialDateShouldBeFirstTimestamp() {
        val scenario = launchFragmentInContainer<ReplayControlFragment>(tenSecondsGame)
        onView(withId(R.id.date)).check(matches(withText(firstDate.systemDate)))
        scenario.onFragment {
            assertEquals(0, it.timeSelectionBar.progress)
        }
    }

    @Test
    fun initiallyAllElementsAreDisplayed() {
        launchFragmentInContainer<ReplayControlFragment>(tenSecondsGame)

        listOf(
                R.id.playButton, R.id.stopButton,
                R.id.speedLabel, R.id.speedLabel, R.id.speedSelection,
                R.id.speedFactor, R.id.date, R.id.timeSelectionBar
        ).forEach {
            onView(withId(it)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun eachTimeCursorStepShouldIncreaseTheDateByOneSecond() {
        val scenario = launchFragmentInContainer<ReplayControlFragment>(tenSecondsGame)

        scenario.onFragment {
            assertEquals(it.timeSelectionBar.min, it.timeSelectionBar.progress)
        }

        val timeRange = 0..(tenSeconds.timestamp - firstDate.timestamp)
        for (time in timeRange) {
            onView(withId(R.id.timeSelectionBar)).perform(clickSeekBar(time.toInt()))
            onView(withId(R.id.date)).check(matches(withText(DateTS(firstDate.timestamp + time).systemDate)))
        }

        scenario.onFragment {
            assertEquals(it.timeSelectionBar.max, it.timeSelectionBar.progress)
        }
    }


    @Test
    fun speedFactorArePowersOfTwoFrom0To32() {
        val scenario = launchFragmentInContainer<ReplayControlFragment>(tenSecondsGame)

        var maxSpeedIndex = 0
        scenario.onFragment {
            it.speedSelection.progress = 0
            assertEquals(it.speedSelection.min, it.speedSelection.progress)
            maxSpeedIndex = it.speedSelection.max
        }

        for (pos in 0..maxSpeedIndex) {
            onView(withId(R.id.speedSelection)).perform(clickSeekBar(pos))
            val expectedFactor = "x" + if (pos == 0) "0" else 2f.pow(pos - 1).toInt().toString()
            onView(withId(R.id.speedFactor)).check(matches(withText(expectedFactor)))
        }

        scenario.onFragment {
            assertEquals(it.speedSelection.max, it.speedSelection.progress)
        }
    }

    @Test
    fun playButtonSetsSpeedTo1() {
        val scenario = launchFragmentInContainer<ReplayControlFragment>(tenSecondsGame)
        scenario.onFragment { it.speedSelection.progress = 0 }
        onView(withId(R.id.speedFactor)).check(matches(withText("x0")))
        onView(withId(R.id.playButton)).perform(click())
        onView(withId(R.id.speedFactor)).check(matches(withText("x1")))
    }

    @Test
    fun stopButtonSetsSpeedTo0() {
        val scenario = launchFragmentInContainer<ReplayControlFragment>(tenSecondsGame)
        scenario.onFragment { it.speedSelection.progress = 1 }
        onView(withId(R.id.speedFactor)).check(matches(withText("x1")))
        onView(withId(R.id.stopButton)).perform(click())
        onView(withId(R.id.speedFactor)).check(matches(withText("x0")))
    }

    @Test
    fun playSpeedCorrespondsToSpeedFactor() {
        val scenario = launchFragmentInContainer<ReplayControlFragment>(longGame)

        var maxSpeedIndex = 0
        scenario.onFragment {
            it.speedSelection.progress = 0
            assertEquals(it.speedSelection.min, it.speedSelection.progress)
            maxSpeedIndex = it.speedSelection.max
        }

        for (pos in 0..maxSpeedIndex) {
            val expectedProgress = if (pos == 0) 0.0 else 2.0.pow(pos - 1)
            scenario.onFragment {
                it.speedSelection.progress = 0
                it.timeSelectionBar.progress = 0
                it.speedSelection.progress = pos
            }

            Thread.sleep(1100) // 1sec + 10% for latency tolerance

            scenario.onFragment {
                it.speedSelection.progress = 0

                assertEquals(expectedProgress, it.timeSelectionBar.progress.toDouble(), expectedProgress / 10)
            }
        }
    }
}