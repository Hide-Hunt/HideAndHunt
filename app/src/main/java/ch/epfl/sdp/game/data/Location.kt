package ch.epfl.sdp.game.data

class Location(var latitude: Double, var longitude: Double) {
    fun distanceTo(other: Location): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(latitude, longitude, other.latitude, other.longitude, results)
        return results[0]
    }

    override fun toString(): String {
        return "Location(latitude=$latitude, longitude=$longitude)"
    }
}