package ch.epfl.sdp.game.data

import kotlin.math.*

class Location(var latitude: Double, var longitude: Double) {
    fun distanceTo(other: Location): Double {
        return sqrt(
                (latitude - other.latitude).pow(2.0) + (longitude - other.longitude).pow(2.0)
        )
    }
}