package ch.epfl.sdp.utils

import ch.epfl.sdp.game.data.Area
import ch.epfl.sdp.game.data.Location
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.LatLong

/**
 * Convert the location to a [LatLong]
 */
fun Location.toLatLong() = LatLong(latitude, longitude)

/**
 * Convert a [LatLong] to a location
 */
fun LatLong.toLocation() = Location(latitude, longitude)

/**
 * Convert an [Area] to a [BoundingBox]
 * @return BoundingBox: The resulting [BoundingBox] corresponding to the Area
 */
fun Area.toBoundingBox(): BoundingBox {
    return BoundingBox(bottomLeft.latitude, bottomLeft.longitude, topRight.latitude, topRight.longitude)
}