package ch.epfl.sdp.game

import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Player
import org.junit.Test

import org.junit.Assert.*

class PlayerTest {
    @Test
    fun testToStringWithoutLocation() {
        val p = Player(42)
        assertEquals("Player{id=42, lastKnownLocation=null}", p.toString())
    }

    @Test
    fun testToStringWithLocation() {
        val p = Player(42)
        p.lastKnownLocation = Location(latitude = 42.0, longitude = 24.0)
        assertEquals("Player{id=42, lastKnownLocation=Location(latitude=42.0, longitude=24.0)}", p.toString())
    }
}