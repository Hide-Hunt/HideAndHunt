package ch.epfl.sdp.replay.steps

import androidx.core.graphics.drawable.toBitmap
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.utils.toLatLong
import org.junit.Test
import org.mapsforge.core.graphics.Color
import org.mapsforge.core.graphics.Paint
import org.mapsforge.core.graphics.Style
import org.mapsforge.map.android.graphics.AndroidBitmap
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.layer.overlay.Marker
import org.mapsforge.map.layer.overlay.Polyline

class LocationStepTest {

    private fun createPaint(color: Int, strokeWidth: Int, style: Style?): Paint {
        val paint: Paint = AndroidGraphicFactory.INSTANCE.createPaint()
        paint.color = color
        paint.strokeWidth = strokeWidth.toFloat()
        paint.setStyle(style)
        return paint
    }

    @Test
    fun executeOneStepShouldUpdateMarker() {
        val icon = AndroidBitmap(InstrumentationRegistry.getInstrumentation().targetContext
                .getDrawable(R.drawable.ic_target_icon)!!.toBitmap())
        val loc = Location(0.0, 0.0)
        val newloc = Location(1.0, 0.0)
        val marker = Marker(loc.toLatLong(), icon, 0, 0)
        val myPoly = Polyline(createPaint(
                AndroidGraphicFactory.INSTANCE.createColor(Color.values().random()),
                4, Style.STROKE), AndroidGraphicFactory.INSTANCE)
        val step = LocationStep(marker, myPoly, loc, newloc)

        step.execute()

        assert( marker.position.latitude == newloc.latitude &&
                marker.position.longitude == newloc.longitude)
    }

    @Test
    fun undoOneStepShouldRevertMarker() {
        val icon = AndroidBitmap(InstrumentationRegistry.getInstrumentation().targetContext
                .getDrawable(R.drawable.ic_target_icon)!!.toBitmap())
        val loc = Location(0.0, 0.0)
        val newloc = Location(1.0, 0.0)
        val marker = Marker(loc.toLatLong(), icon, 0, 0)
        val myPoly = Polyline(createPaint(
                AndroidGraphicFactory.INSTANCE.createColor(Color.values().random()),
                4, Style.STROKE), AndroidGraphicFactory.INSTANCE)
        val step = LocationStep(marker, myPoly, loc, newloc)

        step.execute()
        step.undo()

        assert( marker.position.latitude == loc.latitude &&
                marker.position.longitude == loc.longitude)    }
}