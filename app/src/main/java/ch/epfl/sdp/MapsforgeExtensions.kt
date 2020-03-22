package ch.epfl.sdp

import ch.epfl.sdp.game.data.Location
import org.mapsforge.core.model.LatLong

fun Location.toLatLong() = LatLong(latitude, longitude)
fun LatLong.toLocation() = Location(latitude, longitude)