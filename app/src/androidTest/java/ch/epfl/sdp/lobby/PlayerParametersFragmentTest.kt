package ch.epfl.sdp.lobby

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.R
import ch.epfl.sdp.game.PlayerFaction
import org.junit.Test

class PlayerParametersFragmentTest {

    @Test
    fun testFactionListen() {
        val scenario = launchFragmentInContainer<PlayerParametersFragment>()
        var callbackCalled = false
        val defaultValue = PlayerFaction.PREDATOR
        var isValueCorrect = false
        val listener = object : PlayerParametersFragment.OnFactionChangeListener {
            override fun onFactionChange(newFaction: PlayerFaction) {
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
        val defaultValue = PlayerFaction.PREDATOR
        var isValueCorrect = false
        val listener = object : PlayerParametersFragment.OnFactionChangeListener {
            override fun onFactionChange(newFaction: PlayerFaction) {
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