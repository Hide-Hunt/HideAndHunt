package ch.epfl.sdp.game

import ch.epfl.sdp.game.data.Location
import org.junit.Test

import org.junit.Assert.*

class LocationTest {
    @Test
    fun distanceToWithSamePointShouldReturnZero() {
        val a = Location(42.0, 24.0)
        val b = Location(42.0, 24.0)
        assertEquals(0.0, a.distanceTo(b), 0.0001)
    }

    @Test
    fun distanceToAPointShouldBeMathematicallyCorrect() {
        val a = Location(9.0, 26.0)
        val b = Location(12.0, 22.0)
        assertEquals(5.0, a.distanceTo(b), 0.0001)
    }
}