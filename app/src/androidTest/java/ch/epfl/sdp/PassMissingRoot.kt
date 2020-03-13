package ch.epfl.sdp

import android.view.View
import androidx.test.espresso.FailureHandler
import androidx.test.espresso.NoMatchingRootException
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher


class PassMissingRoot : FailureHandler {
    private val defaultHandler: FailureHandler = DefaultFailureHandler(InstrumentationRegistry.getInstrumentation().targetContext)
    override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
        if (error !is NoMatchingRootException) {
            defaultHandler.handle(error, viewMatcher)
        }
    }
}