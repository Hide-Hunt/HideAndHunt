package ch.epfl.sdp.utils

import ch.epfl.sdp.game.data.Area
import ch.epfl.sdp.game.data.Location
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.LatLong

fun Location.toLatLong() = LatLong(latitude, longitude)
fun LatLong.toLocation() = Location(latitude, longitude)
fun Area.toBoundingBox() : BoundingBox {
    return BoundingBox(bottomLeft.latitude, bottomLeft.longitude, topRight.latitude, topRight.longitude)
}