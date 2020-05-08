package ch.epfl.sdp

import android.widget.SeekBar
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.*
import java.lang.Float.min

object EspressoViewActions {
    fun clickSeekBar(pos: Int): ViewAction {
        val coordinatesProvider = CoordinatesProvider { view ->
            val seekBar = view as SeekBar

            val screenPos = intArrayOf(0, 0)
            seekBar.getLocationOnScreen(screenPos)

            // get the width of the actual bar area by removing padding
            val trueWidth = seekBar.width - seekBar.paddingLeft - seekBar.paddingRight

            // what is the position on a 0-1 scale add 0.3f to avoid roundoff to the next smaller position
            val relativePos = min((0.3f + pos)/ seekBar.max.toFloat(), 1.0f)

            // determine where to click
            val screenX = trueWidth*relativePos + screenPos[0] + seekBar.paddingLeft
            val screenY = seekBar.height /2f + screenPos[1]

            floatArrayOf(screenX, screenY)
        }

        return GeneralClickAction(Tap.SINGLE, coordinatesProvider, Press.FINGER)
    }
}