package ch.epfl.sdp

import android.view.View
import org.hamcrest.Matcher

object EspressoTestsMatchers {
    fun withDrawable(resourceId: Int): Matcher<View> {
        return DrawableMatcher(resourceId)
    }

    fun noDrawable(): Matcher<View> {
        return DrawableMatcher(-1)
    }
}