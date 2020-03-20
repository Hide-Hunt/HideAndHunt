package ch.epfl.sdp.game.data

import org.junit.Assert.*
import org.junit.Test

class AreaTest {
    @Test
    fun nullAreaIsNull() {
        val area = Area(Location(0.0,0.0), Location(0.0, 0.0))
        assertEquals(0.0, area.topRight.latitude, 0.0)
        assertEquals(0.0, area.topRight.longitude, 0.0)
        assertEquals(0.0, area.bottomLeft.latitude, 0.0)
        assertEquals(0.0, area.bottomLeft.longitude, 0.0)
        assertEquals(0.0, area.center.latitude, 0.0)
        assertEquals(0.0, area.center.longitude, 0.0)
    }

    @Test
    fun centerOfAreaIsComputedCorrectly() {
        val area = Area(Location(2.0,0.0), Location(1.0, 2.0))
        assertEquals(1.5, area.center.latitude, 0.0)
        assertEquals(1.0, area.center.longitude, 0.0)

        val area2 = Area(Location(-2.0,-3.0), Location(1.0, 1.0))
        assertEquals(-0.5, area2.center.latitude, 0.0)
        assertEquals(-1.0, area2.center.longitude, 0.0)
    }

    @Test
    fun containedPointMustBeConsideredAsContained() {
        val area = Area(Location(2.0,0.0), Location(1.0, 2.0))
        assertTrue(area.contains(Location(1.5, 1.0)))
        assertTrue(area.contains(Location(2.0, 0.0)))
        assertTrue(area.contains(Location(2.0, 1.0)))
        assertTrue(area.contains(Location(1.0, 0.0)))
        assertTrue(area.contains(Location(1.0, 2.0)))
    }

    @Test
    fun externalPointMustNotBeConsideredAsContained() {
        val area = Area(Location(2.0,0.0), Location(1.0, 2.0))
        assertFalse(area.contains(Location(3.5, 1.0)))
        assertFalse(area.contains(Location(3.0, 0.0)))
        assertFalse(area.contains(Location(3.0, 1.0)))
        assertFalse(area.contains(Location(0.0, 0.0)))
        assertFalse(area.contains(Location(0.0, 2.0)))
    }
}