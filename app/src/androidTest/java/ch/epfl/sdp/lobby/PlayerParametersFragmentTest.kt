package ch.epfl.sdp.lobby

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Faction
import org.junit.Test

class PlayerParametersFragmentTest {

    @Test
    fun testFactionListen() {
        val scenario = launchFragmentInContainer<PlayerParametersFragment>()
        var callbackCalled = false
        val defaultValue = Faction.PREDATOR
        var isValueCorrect = false
        val listener = object : PlayerParametersFragment.OnFactionChangeListener {
            override fun onFactionChange(newFaction: Faction) {
                callbackCalled = true
                isValueCorrect = newFaction == defaultValue
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }
        assert(callbackCalled)
        assert(isValueCorrect)
    }

    @Test
    fun testFactionSwitch() {
        val scenario = launchFragmentInContainer<PlayerParametersFragment>()
        var callbackCalled = false
        var callNb = 0
        val defaultValue = Faction.PREDATOR
        var isValueCorrect = false
        val listener = object : PlayerParametersFragment.OnFactionChangeListener {
            override fun onFactionChange(newFaction: Faction) {
                isValueCorrect = if (callNb == 0)
                    newFaction == defaultValue
                else {
                    callbackCalled = true
                    isValueCorrect && newFaction != defaultValue
                }
                callNb++
            }
        }
        scenario.onFragment { fragment -> fragment.listener = listener }
        onView(withId(R.id.switch_faction)).perform(click())
        assert(callbackCalled)
        assert(isValueCorrect)
    }
}