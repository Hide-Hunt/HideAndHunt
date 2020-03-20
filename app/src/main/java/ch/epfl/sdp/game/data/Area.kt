package ch.epfl.sdp.game.data

class Area(p1: Location, p2: Location) {
    val bottomLeft = Location(
            if (p1.latitude < p2.latitude) p1.latitude else p2.latitude,
            if (p1.longitude < p2.longitude) p1.longitude else p2.longitude
    )

    val topRight = Location(
            if (p1.latitude >= p2.latitude) p1.latitude else p2.latitude,
            if (p1.longitude >= p2.longitude) p1.longitude else p2.longitude
    )

    val center = Location(
            (topRight.latitude + bottomLeft.latitude) / 2,
            (topRight.longitude + bottomLeft.longitude) / 2
    )

    fun contains(point: Location) =
            point.latitude >= bottomLeft.latitude &&
                    point.latitude <= topRight.latitude &&
                    point.longitude >= bottomLeft.longitude &&
                    point.longitude <= topRight.longitude
}