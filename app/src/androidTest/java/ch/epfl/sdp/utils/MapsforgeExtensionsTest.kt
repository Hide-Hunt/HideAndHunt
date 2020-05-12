package ch.epfl.sdp.utils

import org.junit.Assert.*
import org.junit.Test
import org.mapsforge.core.model.LatLong
import ch.epfl.sdp.game.data.Location

class MapsforgeExtensionsTest{
    @Test
    fun testToLocation(){
        val latitude = 0.0008
        val longitude = 0.0007
        val ll = LatLong(latitude, longitude)
        val location = Location(latitude, longitude)

        assert(ll.toLocation() == location)
    }

}