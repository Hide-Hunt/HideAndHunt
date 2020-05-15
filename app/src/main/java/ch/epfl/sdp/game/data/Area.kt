package ch.epfl.sdp.game.data

import java.io.Serializable

/**
 * Describe a map area by giving two point, the top right corner and the bottom left one.
 * The top right and bottom left corner are automatically deduced from the points themselves
 * @param p1 Location: The [Location] of the first point
 * @param p1 Location: The [Location] of the second point
 */
class Area(p1: Location, p2: Location) : Serializable {
    val bottomLeft = Location(
            if (p1.latitude < p2.latitude) p1.latitude else p2.latitude,
            if (p1.longitude < p2.longitude) p1.longitude else p2.longitude
    )

    val topRight = Location(
            if (p1.latitude >= p2.latitude) p1.latitude else p2.latitude,
            if (p1.longitude >= p2.longitude) p1.longitude else p2.longitude
    )

    val center: Location by lazy {
        Location(
                (topRight.latitude + bottomLeft.latitude) / 2.0,
                (topRight.longitude + bottomLeft.longitude) / 2.0
        )
    }

    /**
     * Indicates if a point is in the Area
     * @param point Location: the [Location] to test
     * @return Boolean: True if the point is in the Area, False otherwise
     */
    fun contains(point: Location): Boolean =
            point.latitude >= bottomLeft.latitude &&
                    point.latitude <= topRight.latitude &&
                    point.longitude >= bottomLeft.longitude &&
                    point.longitude <= topRight.longitude

    /**
     * Increase the area by giving a new point, the top right or bottom left corner is
     * automatically replaced.
     * @param point Location: The [Location] of the new point describing the top right or bottom left corner
     * @return Area: The new calculated area
     */
    fun increase(point: Location): Area {
        val newBottomLeft =
                if (point.latitude < bottomLeft.latitude || point.longitude < bottomLeft.longitude)
                    Location(minOf(bottomLeft.latitude, point.latitude),
                            minOf(bottomLeft.longitude, point.longitude))
                else bottomLeft

        val newTopRight =
                if (point.latitude > topRight.latitude || point.longitude > topRight.longitude)
                    Location(maxOf(topRight.latitude, point.latitude),
                            maxOf(topRight.longitude, point.longitude))
                else topRight

        return if (newBottomLeft != bottomLeft || newTopRight != topRight) Area(newBottomLeft, newTopRight) else this
    }
}