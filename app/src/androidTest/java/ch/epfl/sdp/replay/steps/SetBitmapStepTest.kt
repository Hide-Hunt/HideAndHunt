package ch.epfl.sdp.replay.steps

import androidx.core.graphics.drawable.toBitmap
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.utils.toLatLong
import org.junit.Test
import org.mapsforge.map.android.graphics.AndroidBitmap
import org.mapsforge.map.layer.overlay.Marker

class SetBitmapStepTest {
    @Test
    fun executeOneStepShouldUpdateMarker() {
        val oldIcon = AndroidBitmap(InstrumentationRegistry.getInstrumentation().targetContext
                .getDrawable(R.drawable.ic_target_icon)!!.toBitmap())
        val newIcon = AndroidBitmap(InstrumentationRegistry.getInstrumentation().targetContext
                .getDrawable(R.drawable.ic_running_icon)!!.toBitmap())
        val loc = Location(0.0, 0.0)
        val marker = Marker(loc.toLatLong(), oldIcon, 0, 0)
        val step = SetBitmapStep(marker, newIcon)

        step.execute()

        assert(marker.bitmap == newIcon)
    }

    @Test
    fun undoOneStepShouldRevertMarker() {
        val oldIcon = AndroidBitmap(InstrumentationRegistry.getInstrumentation().targetContext
                .getDrawable(R.drawable.ic_target_icon)!!.toBitmap())
        val newIcon = AndroidBitmap(InstrumentationRegistry.getInstrumentation().targetContext
                .getDrawable(R.drawable.ic_running_icon)!!.toBitmap())
        val loc = Location(0.0, 0.0)
        val marker = Marker(loc.toLatLong(), oldIcon, 0, 0)
        val step = SetBitmapStep(marker, newIcon)

        step.execute()
        step.undo()

        assert(marker.bitmap == oldIcon)
    }
}