package ch.epfl.sdp.replay.steps

import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.toLatLong
import org.mapsforge.map.layer.overlay.Marker

class LocationStep(private val overlay: Marker, private val prevLoc: Location, private val nextLoc: Location) : ReplayStep {
    override fun execute() {
        overlay.latLong = nextLoc.toLatLong()
        overlay.requestRedraw()
    }

    override fun undo() {
        overlay.latLong = prevLoc.toLatLong()
        overlay.requestRedraw()
    }
}