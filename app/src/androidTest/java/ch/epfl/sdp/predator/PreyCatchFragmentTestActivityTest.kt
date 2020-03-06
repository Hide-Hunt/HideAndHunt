package ch.epfl.sdp.predator

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.lang.RuntimeException

@RunWith(AndroidJUnit4::class)
class PreyCatchFragmentTestActivityTest {

    @Test
    fun fragmentIsInitialized() {
        val scenario = launchActivity<PreyCatchFragmentTestActivity>()
        scenario.onActivity { activity ->
            assertNotEquals(null, activity.pcf)
        }
    }

    @Test
    fun fragmentFunctionCallDoesNotCrash() {
        val scenario = launchActivity<PreyCatchFragmentTestActivity>()
        scenario.onActivity { activity ->
            activity.pcf.onNfcTagRead(activity.pcf.tagIdToString(ByteArray(10)))
        }
    }
}