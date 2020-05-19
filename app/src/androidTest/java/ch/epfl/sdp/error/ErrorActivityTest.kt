package ch.epfl.sdp.error

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.MainActivity
import ch.epfl.sdp.R
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test

class ErrorActivityTest {
    @Before
    fun setup() {
        Intents.init() }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun activityWithoutIntentShowsUnknownErrorMessage() {
        launchActivity<ErrorActivity>()
        onView(withId(R.id.error_detail)).check(matches(withText(R.string.unknown_error)))
    }

    @Test
    fun activityWithoutErrorShowsUnknownErrorMessage() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, ErrorActivity::class.java)
        launchActivity<ErrorActivity>(intent)
        onView(withId(R.id.error_detail)).check(matches(withText(R.string.unknown_error)))
    }

    @Test
    fun activityWithErrorInIntentShowsGivenErrorMessage() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, ErrorActivity::class.java)
        intent.putExtra("error", Error(ErrorCode.INVALID_ACTIVITY_PARAMETER, "s0m3 3rr0r m5g"))
        launchActivity<ErrorActivity>(intent)
        onView(withId(R.id.error_detail)).check(matches(withText("s0m3 3rr0r m5g")))
    }

    @Test
    fun activityLaunchedWithStartWithShowsGivenErrorMessage() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ErrorActivity.startWith(context, Error(ErrorCode.INVALID_ACTIVITY_PARAMETER, "s0m3 3rr0r m5g"))
        onView(withId(R.id.error_detail)).check(matches(withText("s0m3 3rr0r m5g")))
    }

    @Test
    fun homeButtonReturnsToMainActivity() {
        launchActivity<ErrorActivity>()
        onView(withId(R.id.home_button)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun homeButtonResetsActivityStack() {
        launchActivity<ErrorActivity>()
        onView(withId(R.id.home_button)).perform(click())
        Intents.intended(allOf(
                IntentMatchers.hasComponent(MainActivity::class.java.name),
                IntentMatchers.hasFlag(Intent.FLAG_ACTIVITY_NEW_TASK)))
    }
}