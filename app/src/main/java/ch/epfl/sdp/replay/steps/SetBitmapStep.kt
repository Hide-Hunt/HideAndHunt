package ch.epfl.sdp.replay.steps

import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.map.layer.overlay.Marker

class SetBitmapStep(private val marker: Marker, private val newBitmap: Bitmap) : ReplayStep {
    private lateinit var previousBitmap: Bitmap

    override fun execute() {
        previousBitmap = marker.bitmap
        previousBitmap.incrementRefCount() // Prevent recycling
        marker.bitmap = newBitmap
        marker.requestRedraw()
    }

    override fun undo() {
        marker.bitmap.incrementRefCount() // Prevent recycling
        marker.bitmap = previousBitmap
        marker.requestRedraw()
    }
}