package ch.epfl.sdp.replay.steps

import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.utils.toLatLong
import org.mapsforge.map.layer.overlay.Marker
import org.mapsforge.map.layer.overlay.Polyline

class LocationStep(
        private val marker: Marker,
        private val path: Polyline,
        private val prevLoc: Location,
        private val nextLoc: Location) : ReplayStep {
    override fun execute() {
        marker.latLong = nextLoc.toLatLong()
        marker.requestRedraw()
        path.addPoint(marker.latLong)
        path.requestRedraw()
    }

    override fun undo() {
        marker.latLong = prevLoc.toLatLong()
        marker.requestRedraw()
        path.latLongs.removeAt(path.latLongs.size-1)
        path.requestRedraw()
    }
}