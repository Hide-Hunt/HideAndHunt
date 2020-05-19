package ch.epfl.sdp.db

import ch.epfl.sdp.game.data.Faction
import org.junit.Test

import org.junit.Assert.*

class ConvertersTest {
    @Test
    fun ordinalConvertsToCorrespondingFaction() {
        val converters = Converters()
        assertEquals(Faction.PREY, converters.fromInt(0))
        assertEquals(Faction.PREDATOR, converters.fromInt(1))
    }

    @Test
    fun factionConvertsToCorrespondingOrdinal() {
        val converters = Converters()
        assertEquals(0, converters.factionToInt(Faction.PREY))
        assertEquals(1, converters.factionToInt(Faction.PREDATOR))
    }
}