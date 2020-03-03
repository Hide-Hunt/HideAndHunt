package ch.epfl.sdp.predator

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.android.synthetic.main.prey_catch_fragment_test_activity.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreyCatchFragmentTestActivityTest {

    @Test
    fun fragmentIsInitialized() {
        val scenario = launchActivity<PreyCatchFragmentTestActivity>()
        scenario.onActivity { activity ->
            assert(activity.pcf != null)
        }
    }
}