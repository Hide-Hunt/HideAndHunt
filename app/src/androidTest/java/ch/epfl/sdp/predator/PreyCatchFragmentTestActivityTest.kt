package ch.epfl.sdp.predator

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.lang.RuntimeException

@RunWith(AndroidJUnit4::class)
class PreyCatchFragmentTestActivityTest {


    @get:Rule
    var exceptionRule = ExpectedException.none()

    @Test
    fun fragmentIsInitialized() {
        val scenario = launchActivity<PreyCatchFragmentTestActivity>()
        scenario.onActivity { activity ->
            assert(activity.pcf != null)
        }
    }

    @Test
    fun tagToStringReturnsCorrectValues() {
        val scenario = launchActivity<PreyCatchFragmentTestActivity>()
        scenario.onActivity { activity ->
            val bytes = ByteArray(2)
            bytes[0] = 0xFF.toByte()
            bytes[1] = 0xCA.toByte()
            assert(activity.tagIdToString(bytes) == "FFCA")
        }
    }

    @Test
    fun tagToStringThrowsIfArgumentIsNull() {
        val scenario = launchActivity<PreyCatchFragmentTestActivity>()
        scenario.onActivity { activity ->
            exceptionRule.expect(RuntimeException::class.java)
            activity.tagIdToString(null)
        }
    }
}