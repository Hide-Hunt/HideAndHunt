package ch.epfl.sdp.game.data

import org.junit.Assert.*
import org.junit.Test

class AreaTest {
    private val simpleArea = Area(Location(2.0, 0.0), Location(1.0, 2.0))

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
        fun validateArea(area: Area) {
            assertEquals(0.0, area.topRight.latitude, 1.0)
            assertEquals(0.0, area.topRight.longitude, 1.0)
            assertEquals(0.0, area.bottomLeft.latitude, 0.0)
            assertEquals(0.0, area.bottomLeft.longitude, 0.0)
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
        listOf( // Points inside the area
                Location(1.5, 1.0),
                Location(2.0, 0.0),
                Location(2.0, 1.0),
                Location(1.0, 0.0),
                Location(1.0, 2.0)
        ).forEach { assertTrue(simpleArea.contains(it)) }
    }

    @Test
    fun externalPointMustNotBeConsideredAsContained() {
        listOf( // Points outside the area
                Location(-3.5, -1.0),
                Location(-3.0, 0.0),
                Location(-3.0, -1.0),
                Location(0.0, -2.0),

                Location(3.5, 1.0),
                Location(3.0, 0.0),
                Location(3.0, 1.0),
                Location(0.0, 0.0),
                Location(0.0, 2.0)
        ).forEach { assertFalse(simpleArea.contains(it)) }
    }

    @Test
    fun containedPointsShouldNotIncreaseArea() {
        var area = simpleArea
        listOf( // Points inside the area
                Location(1.5, 1.0),
                Location(2.0, 0.0),
                Location(2.0, 1.0),
                Location(1.0, 0.0),
                Location(1.0, 2.0)
        ).forEach {
            area = area.increase(it)
            assertEquals(simpleArea, area)
        }
    }

    @Test
    fun externalPointsShouldIncreaseArea() {
        val area = simpleArea.increase(Location(0.0, 0.0))
        assertEquals(2.0, area.topRight.latitude, 0.0)
        assertEquals(2.0, area.topRight.longitude, 0.0)
        assertEquals(0.0, area.bottomLeft.latitude, 0.0)
        assertEquals(0.0, area.bottomLeft.longitude, 0.0)

        val area2 = simpleArea.increase(Location(2.0, 3.0))
        assertEquals(2.0, area2.topRight.latitude, 0.0)
        assertEquals(3.0, area2.topRight.longitude, 0.0)
        assertEquals(1.0, area2.bottomLeft.latitude, 0.0)
        assertEquals(0.0, area2.bottomLeft.longitude, 0.0)

        val area3 = simpleArea.increase(Location(4.0, -1.0))
        assertEquals(4.0, area3.topRight.latitude, 0.0)
        assertEquals(2.0, area3.topRight.longitude, 0.0)
        assertEquals(1.0, area3.bottomLeft.latitude, 0.0)
        assertEquals(-1.0, area3.bottomLeft.longitude, 0.0)
    }
}