package ch.epfl.sdp.replay.game_history

import android.util.Base64
import ch.epfl.sdp.game.data.Predator
import ch.epfl.sdp.game.data.Prey
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.ExpectedException
import java.io.ByteArrayInputStream
import java.io.InputStream

class GameHistoryTest {
    /* To generate file replay content, use:
        protoc --encode=ch.epfl.sdp.game.comm.Game $PROTO_FILE < $GAME_FILE | base64

        Where:
            $PROTO_FILE is the protocol buffer message definition (probably Game.proto)
            $GAME_FILE is a text version in the protoc format of the replay
     */

    /*
        id: 42
        name: "Some game name"
        adminID: 1
        start_timestamp: 1583
        end_timestamp: 2494
        players: [
                { id: 0; faction: PREY },
                { id: 1; faction: PREDATOR }
        ]
        events: [
            { timestamp: 1583; location_event: { playerID: 0; location: { latitude: 46.48; longitude: 6.56 } } },
            { timestamp: 1584; location_event: { playerID: 1; location: { latitude: 46.51; longitude: 6.51 } } },
            { timestamp: 2494; catch_event:    { predatorID: 1; preyID: 0 } }
        }
     */
    private val veryShortGame = "CCoSDlNvbWUgZ2FtZSBuYW1lGAElLwYAAC2+CQAAUgBSBAgBEAFaGw0vBgAAWhQSEgk9CtejcD1H" +
            "QBE9CtejcD0aQFodDTAGAABaFggBEhIJ4XoUrkdBR0ARCtejcD0KGkBaCQ2+CQAAUgIIAQ=="

    /*
        id: 37
        name: "Some game name"
        adminID: 4
        start_timestamp: 8315
        end_timestamp: 9424
        players: [
                { id: 0; faction: PREDATOR },
                { id: 1; faction: PREY }
        ]
        events: [
                { timestamp: 8315; location_event: { playerID: 1; location: { latitude: 46.67; longitude: 6.51 } } },
                { timestamp: 8316; location_event: { playerID: 0; location: { latitude: 46.51; longitude: 6.56 } } },
                { timestamp: 9424; catch_event:    { predatorID: 0; preyID: 1 } }
        ]
     */
    private val veryShortGame2 = "CCUSDlNvbWUgZ2FtZSBuYW1lGAQleyAAAC3QJAAAUgIQAVICCAFaHQ17IAAAWhYIARISCfYoXI/C" +
            "VUdAEQrXo3A9ChpAWhsNfCAAAFoUEhIJ4XoUrkdBR0ARPQrXo3A9GkBaCQ3QJAAAUgIQAQ=="

    private fun gameAsInputStream(b64Game: String): InputStream =
            ByteArrayInputStream(Base64.decode(b64Game, Base64.DEFAULT))

    @get:Rule
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun fileWithNegativeIDShouldRaiseInvalidGameFormatException() {
        /*
        id: -13
        name: "Some game name"
        adminID: 1
        start_timestamp: 1583
        end_timestamp: 2494
        players: [ { id: 0; faction: PREY } ]
        events: [ { timestamp: 1583; location_event: { playerID: 0; location: { latitude: 46.48; longitude: 6.56 } } } ]
         */
        val invalidGameID =
                "CPP//////////wESDlNvbWUgZ2FtZSBuYW1lGAElLwYAAC2+CQAAUgBaGw0vBgAAWhQSEgk9Ctej" +
                "cD1HQBE9CtejcD0aQA=="
        exception.expect(GameHistory.InvalidGameFormat::class.java)
        exception.expectMessage("Invalid game id")
        GameHistory.fromFile(gameAsInputStream(invalidGameID))
    }

    @Test
    fun fileWithNegativeAdminIDShouldRaiseInvalidGameFormatException() {
        /*
        id: 42
        name: "Some game name"
        adminID: -7
        start_timestamp: 1583
        end_timestamp: 2494
        players: [ { id: 0; faction: PREY } ]
        events: [ { timestamp: 1583; location_event: { playerID: 0; location: { latitude: 46.48; longitude: 6.56 } } } ]
         */
        val invalidGameID =
                "CCoSDlNvbWUgZ2FtZSBuYW1lGPn//////////wElLwYAAC2+CQAAUgBaGw0vBgAAWhQSEgk9Ctej" +
                "cD1HQBE9CtejcD0aQA=="
        exception.expect(GameHistory.InvalidGameFormat::class.java)
        exception.expectMessage("Invalid admin id")
        GameHistory.fromFile(gameAsInputStream(invalidGameID))
    }

    @Test
    fun fileWithoutPlayerShouldRaiseInvalidGameFormatException() {
        /*
        id: 42
        name: "Some game name"
        adminID: 1
        start_timestamp: 1583
        end_timestamp: 2494
        players: [ ]
        events: [ { timestamp: 1583; location_event: { playerID: 0; location: { latitude: 46.48; longitude: 6.56 } } } ]
         */
        val invalidGameID =
                "CCoSDlNvbWUgZ2FtZSBuYW1lGAElLwYAAC2+CQAAWhsNLwYAAFoUEhIJPQrXo3A9R0ARPQrXo3A9" +
                "GkA="
        exception.expect(GameHistory.InvalidGameFormat::class.java)
        exception.expectMessage("No player")
        GameHistory.fromFile(gameAsInputStream(invalidGameID))
    }

    @Test
    fun fileWithoutEventShouldRaiseInvalidGameFormatException() {
        /*
        id: 42
        name: "Some game name"
        adminID: 1
        start_timestamp: 1583
        end_timestamp: 2494
        players: [ ]
        events: [ { timestamp: 1583; location_event: { playerID: 0; location: { latitude: 46.48; longitude: 6.56 } } } ]
         */
        val invalidGameID = "CCoSDlNvbWUgZ2FtZSBuYW1lGAElLwYAAC2+CQAAUgA="
        exception.expect(GameHistory.InvalidGameFormat::class.java)
        exception.expectMessage("No event")
        GameHistory.fromFile(gameAsInputStream(invalidGameID))
    }

    @Test
    fun parsingValidFileShouldGiveCorrectGameID() {
        GameHistory.fromFile(gameAsInputStream(veryShortGame)).let {
            assertEquals(42, it.gameID)
        }
        GameHistory.fromFile(gameAsInputStream(veryShortGame2)).let {
            assertEquals(37, it.gameID)
        }
    }

    @Test
    fun parsingValidFileShouldGiveCorrectAdminID() {
        GameHistory.fromFile(gameAsInputStream(veryShortGame)).let {
            assertEquals(1, it.adminID)
        }
        GameHistory.fromFile(gameAsInputStream(veryShortGame2)).let {
            assertEquals(4, it.adminID)
        }
    }

    @Test
    fun parsingValidFileShouldGiveCorrectPlayers() {
        GameHistory.fromFile(gameAsInputStream(veryShortGame)).let {
            assertEquals(2, it.players.size)
            it.players[0].let {p ->
                assertEquals(0, p.id)
                assertTrue(p is Prey)
                assertEquals(6.56,  p.lastKnownLocation!!.longitude, 0.001)
                assertEquals(46.48, p.lastKnownLocation!!.latitude, 0.001)
            }
            it.players[1].let {p ->
                assertEquals(1, p.id)
                assertTrue(p is Predator)
                assertEquals(46.51, p.lastKnownLocation!!.latitude, 0.001)
                assertEquals(6.51,  p.lastKnownLocation!!.longitude, 0.001)
            }
        }
        GameHistory.fromFile(gameAsInputStream(veryShortGame2)).let {
            assertEquals(2, it.players.size)
            it.players[0].let {p ->
                assertEquals(0, p.id)
                assertTrue(p is Predator)
                assertEquals(6.56,  p.lastKnownLocation!!.longitude, 0.001)
                assertEquals(46.51, p.lastKnownLocation!!.latitude, 0.001)
            }
            it.players[1].let {p ->
                assertEquals(1, p.id)
                assertTrue(p is Prey)
                assertEquals(46.67, p.lastKnownLocation!!.latitude, 0.001)
                assertEquals(6.51,  p.lastKnownLocation!!.longitude, 0.001)
            }
        }
    }

    @Test
    fun parsingValidFileShouldGiveCorrectBounds() {
        GameHistory.fromFile(gameAsInputStream(veryShortGame)).let {
            assertEquals(46.51, it.bounds.topRight.latitude, 0.001)
            assertEquals(6.56,  it.bounds.topRight.longitude, 0.001)
            assertEquals(46.48, it.bounds.bottomLeft.latitude, 0.001)
            assertEquals(6.51,  it.bounds.bottomLeft.longitude, 0.001)
        }
        GameHistory.fromFile(gameAsInputStream(veryShortGame2)).let {
            assertEquals(46.67, it.bounds.topRight.latitude, 0.001)
            assertEquals(6.56,  it.bounds.topRight.longitude, 0.001)
            assertEquals(46.51, it.bounds.bottomLeft.latitude, 0.001)
            assertEquals(6.51,  it.bounds.bottomLeft.longitude, 0.001)
        }
    }

    @Test
    fun parsingValidFileShouldGiveCorrectEvents() {
        GameHistory.fromFile(gameAsInputStream(veryShortGame)).let {
            assertEquals(3, it.events.size)
            assertTrue(it.events[0] is LocationEvent)
            (it.events[0] as LocationEvent).let {e ->
                assertEquals(1583, e.timestamp)
                assertEquals(0, e.playerID)
                assertEquals(46.48, e.location.latitude, 0.001)
                assertEquals(6.56,  e.location.longitude, 0.001)
            }
            assertTrue(it.events[1] is LocationEvent)
            (it.events[1] as LocationEvent).let {e ->
                assertEquals(1584, e.timestamp)
                assertEquals(1, e.playerID)
                assertEquals(46.51, e.location.latitude, 0.001)
                assertEquals(6.51,  e.location.longitude, 0.001)
            }
            assertTrue(it.events[2] is CatchEvent)
            (it.events[2] as CatchEvent).let {e ->
                assertEquals(2494, e.timestamp)
                assertEquals(1, e.predatorID)
                assertEquals(0, e.preyID)
            }
        }
        GameHistory.fromFile(gameAsInputStream(veryShortGame2)).let {
            assertEquals(3, it.events.size)
            assertTrue(it.events[0] is LocationEvent)
            (it.events[0] as LocationEvent).let {e ->
                assertEquals(8315, e.timestamp)
                assertEquals(1, e.playerID)
                assertEquals(46.67, e.location.latitude, 0.001)
                assertEquals(6.51,  e.location.longitude, 0.001)
            }
            assertTrue(it.events[1] is LocationEvent)
            (it.events[1] as LocationEvent).let {e ->
                assertEquals(8316, e.timestamp)
                assertEquals(0, e.playerID)
                assertEquals(46.51, e.location.latitude, 0.001)
                assertEquals(6.56,  e.location.longitude, 0.001)
            }
            assertTrue(it.events[2] is CatchEvent)
            (it.events[2] as CatchEvent).let {e ->
                assertEquals(9424, e.timestamp)
                assertEquals(0, e.predatorID)
                assertEquals(1, e.preyID)
            }
        }
    }
}