package ch.epfl.sdp.game.data

import org.junit.Assert.*
import org.junit.Test

class AreaTest {
    val simpleArea = Area(Location(2.0, 0.0), Location(1.0, 2.0))

    @Test
    fun nullAreaIsNull() {
        val area = Area(Location(0.0, 0.0), Location(0.0, 0.0))
        assertEquals(0.0, area.topRight.latitude, 0.0)
        assertEquals(0.0, area.topRight.longitude, 0.0)
        assertEquals(0.0, area.bottomLeft.latitude, 0.0)
        assertEquals(0.0, area.bottomLeft.longitude, 0.0)
        assertEquals(0.0, area.center.latitude, 0.0)
        assertEquals(0.0, area.center.longitude, 0.0)
    }

    @Test
    fun areaConstructionIsIndependentOfPointOrder() {
        fun validateArea(area: Area) = area.let {
            assertEquals(0.0, it.topRight.latitude, 1.0)
            assertEquals(0.0, it.topRight.longitude, 1.0)
            assertEquals(0.0, it.bottomLeft.latitude, 0.0)
            assertEquals(0.0, it.bottomLeft.longitude, 0.0)
        }

        // TopRight + BottomLeft
        validateArea(Area(Location(1.0, 1.0), Location(0.0, 0.0)))

        // TopLeft + BottomRight
        validateArea(Area(Location(1.0, 0.0), Location(0.0, 1.0)))

        // BottomLeft + TopRight
        validateArea(Area(Location(0.0, 0.0), Location(1.0, 1.0)))

        // BottomRight + TopLeft
        validateArea(Area(Location(0.0, 1.0), Location(1.0, 0.0)))
    }

    @Test
    fun centerOfAreaIsComputedCorrectly() {
        assertEquals(1.5, simpleArea.center.latitude, 0.0)
        assertEquals(1.0, simpleArea.center.longitude, 0.0)

        val area2 = Area(Location(-2.0, -3.0), Location(1.0, 1.0))
        assertEquals(-0.5, area2.center.latitude, 0.0)
        assertEquals(-1.0, area2.center.longitude, 0.0)
    }

    @Test
    fun containedPointMustBeConsideredAsContained() {
        listOf(
                Location(1.5, 1.0),
                Location(2.0, 0.0),
                Location(2.0, 1.0),
                Location(1.0, 0.0),
                Location(1.0, 2.0)
        ).forEach { assertTrue(simpleArea.contains(it)) }
    }

    @Test
    fun externalPointMustNotBeConsideredAsContained() {
        listOf(
                Location(3.5, 1.0),
                Location(3.0, 0.0),
                Location(3.0, 1.0),
                Location(0.0, 0.0),
                Location(0.0, 2.0)
        ).forEach { assertFalse(simpleArea.contains(it)) }
    }
}