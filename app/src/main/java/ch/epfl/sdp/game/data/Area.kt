package ch.epfl.sdp.game.data

import java.io.Serializable

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

    fun contains(point: Location) =
            point.latitude >= bottomLeft.latitude &&
                    point.latitude <= topRight.latitude &&
                    point.longitude >= bottomLeft.longitude &&
                    point.longitude <= topRight.longitude

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