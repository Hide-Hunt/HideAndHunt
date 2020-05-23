package ch.epfl.sdp.game.data

/**
 * Represents a location
 * @param latitude Double: Location's latitude
 * @param longitude Double: Location's longitude
 */
class Location(var latitude: Double, var longitude: Double) {

    /**
     * Calculate the distance between the current location and a given one
     * @param other Location: the other location
     * @return Float: the distance between the two locations
     */
    fun distanceTo(other: Location): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(latitude, longitude, other.latitude, other.longitude, results)
        return results[0]
    }

    override fun toString(): String {
        return "Location(latitude=$latitude, longitude=$longitude)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }
}