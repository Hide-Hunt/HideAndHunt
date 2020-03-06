package ch.epfl.sdp.game

import org.junit.Test

import org.junit.Assert.*

class PlayerTest {

    @Test
    fun testToStringWithoutLocation() {
        val p = Player(42, Faction.PREDATOR)
        assertEquals("Player{id=42, faction=PREDATOR, lastKnownLocation=null}", p.toString())
    }
}