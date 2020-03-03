package ch.epfl.sdp.predator

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreyCatchFragmentTest {
    @Test
    fun initialTextStatesThePreyIsAlive() {
        val scenario = launchFragmentInContainer<PreyCatchFragment>()
        onView(withId(R.id.prey_state)).check(ViewAssertions.matches(withText(R.string.prey_alive)))
    }

    @Test
    fun scanningTagWhenNoTargetIsSetShouldNotCauseStateChange() {
        val scenario = launchFragmentInContainer<PreyCatchFragment>()
        scenario.onFragment { fragment -> fragment.onNfcTagRead("anything") }
        onView(withId(R.id.prey_state)).check(ViewAssertions.matches(withText(R.string.prey_alive)))
    }

    @Test
    fun scanningEmptyTagShouldNotCauseStateChange() {
        val scenario = launchFragmentInContainer<PreyCatchFragment>()
        scenario.onFragment { fragment -> fragment.onNfcTagRead("") }
        onView(withId(R.id.prey_state)).check(ViewAssertions.matches(withText(R.string.prey_alive)))
    }

    @Test
    fun scanningWrongTagShouldNotCauseStateChange() {
        val scenario = launchFragmentInContainer<PreyCatchFragment>()
        scenario.onFragment { fragment -> fragment.targetTag = "tag" }
        scenario.onFragment { fragment -> fragment.onNfcTagRead("tagtag") }
        onView(withId(R.id.prey_state)).check(ViewAssertions.matches(withText(R.string.prey_alive)))
    }

    @Test
    fun scanningTargetTagShouldCauseStateChange() {
        val scenario = launchFragmentInContainer<PreyCatchFragment>()
        onView(withId(R.id.prey_state)).check(ViewAssertions.matches(withText(R.string.prey_alive)))
        scenario.onFragment { fragment -> fragment.targetTag = "tag" }
        scenario.onFragment { fragment -> fragment.onNfcTagRead("tag") }
        onView(withId(R.id.prey_state)).check(ViewAssertions.matches(withText(R.string.prey_dead)))
    }
}