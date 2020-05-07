package ch.epfl.sdp.replay.game_history

import org.junit.Test

import org.junit.Assert.*

class GameHistoryTest {
    /* To generate this content, use:
        protoc --encode=ch.epfl.sdp.game.comm.Game $PROTO_FILE < $GAME_FILE | base64

        Where:
            $PROTO_FILE is the protocol buffer message definition
            $GAME_FILE is a text version in the protoc format of the replay

            protoc text version should look like this:
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
                        { timestamp: 1583; location_event: { playerID: 0; location: { latitude: 46.51; longitude: 6.56 } } },
                        { timestamp: 1584; location_event: { playerID: 1; location: { latitude: 46.51; longitude: 6.56 } } },
                        ...
                        { timestamp: 2494; catch_event:    { predatorID: 1; preyID: 0 } }
                        ...
                    ]
     */
    val mock_file = "CCoSC1J1biB0byBTYXQhGAEl2Ih2Xi0niXZeUgBSBAgBEAFaGw3aiHZeWhQSEgnkaea2akJHQBGI" +
            "9NvXgUMaQFodDeCIdl5aFggBEhIJ5GnmtmpCR0ARiPTb14FDGkBaGw3giHZeWhQSEgl88rBQa0JH" +
            "QBEEgFAEXkMaQFodDeKIdl5aFggBEhIJfPKwUGtCR0ARBIBQBF5DGkBaGw3iiHZeWhQSEgkTe3vq" +
            "a0JHQBF+KKv8RUMaQFodDeWIdl5aFggBEhIJE3t76mtCR0ARfiir/EVDGkBaGw3liHZeWhQSEgke" +
            "waBfbkJHQBGvTyZbSkMaQFodDeeIdl5aFggBEhIJHsGgX25CR0ARr08mW0pDGkBaGw3niHZeWhQS" +
            "EgnaNTR4ckJHQBGc2cGbSEMaQFodDemIdl5aFggBEhIJ2jU0eHJCR0ARnNnBm0hDGkBaGw3piHZe" +
            "WhQSEglEw08JdUJHQBEzbf/KSkMaQFodDeuIdl5aFggBEhIJRMNPCXVCR0ARM23/ykpDGkBaGw3r" +
            "iHZeWhQSEgl8D5ccd0JHQBGkFHR7SUMaQFodDe2Idl5aFggBEhIJfA+XHHdCR0ARpBR0e0lDGkBa" +
            "Gw3tiHZeWhQSEgnmnLKteUJHQBGc2cGbSEMaQFodDe+Idl5aFggBEhIJ5pyyrXlCR0ARnNnBm0hD" +
            "GkBaGw3viHZeWhQSEglCv9wUfEJHQBGkFHR7SUMaQFodDfGIdl5aFggBEhIJQr/cFHxCR0ARpBR0" +
            "e0lDGkBaGw3xiHZeWhQSEgk5mhBgfkJHQBFilHopT0MaQFodDfOIdl5aFggBEhIJOZoQYH5CR0AR" +
            "YpR6KU9DGkBaGw3ziHZeWhQSEglVQLRpf0JHQBFGHTAiZEMaQFodDfaIdl5aFggBEhIJVUC0aX9C" +
            "R0ARRh0wImRDGkBaGw32iHZeWhQSEglPG+i0gUJHQBEN2OivakMaQFodDfeIdl5aFggBEhIJTxvo" +
            "tIFCR0ARDdjor2pDGkBaGw33iHZeWhQSEgk1iyrWg0JHQBEEgFAEXkMaQFodDfmIdl5aFggBEhIJ" +
            "NYsq1oNCR0ARBIBQBF5DGkBaGw35iHZeWhQSEgn5L/04h0JHQBHxCexEXEMaQFodDfuIdl5aFggB" +
            "EhIJ+S/9OIdCR0AR8QnsRFxDGkBaGw37iHZeWhQSEgl3KAr0iUJHQBHLHSPGWEMaQFodDf2Idl5a" +
            "FggBEhIJdygK9IlCR0ARyx0jxlhDGkBaGw39iHZeWhQSEgmU2SCTjEJHQBEVMIFbd0MaQFodDQCJ" +
            "dl5aFggBEhIJlNkgk4xCR0ARFTCBW3dDGkBaGw0AiXZeWhQSEgkZAm2YjkJHQBGkpfJ2hEMaQFod" +
            "DQSJdl5aFggBEhIJGQJtmI5CR0ARpKXydoRDGkBaGw0EiXZeWhQSEglblNkgk0JHQBF+8/WPmEMa" +
            "QFodDQaJdl5aFggBEhIJW5TZIJNCR0ARfvP1j5hDGkBaGw0GiXZeWhQSEgmCgKKflkJHQBGk374O" +
            "nEMaQFodDQiJdl5aFggBEhIJgoCin5ZCR0ARpN++DpxDGkBaGw0IiXZeWhQSEgm611xImkJHQBEz" +
            "OEpenUMaQFodDQqJdl5aFggBEhIJutdcSJpCR0ARMzhKXp1DGkBaGw0KiXZeWhQSEgnCEg8om0JH" +
            "QBECaIFjvEMaQFodDQyJdl5aFggBEhIJwhIPKJtCR0ARAmiBY7xDGkBaGw0MiXZeWhQSEgkbKsb5" +
            "m0JHQBHLXAaJ2kMaQFodDQ+Jdl5aFggBEhIJGyrG+ZtCR0ARy1wGidpDGkBaGw0PiXZeWhQSEglZ" +
            "pkxXnUJHQBGRF78W4UMaQFodDRGJdl5aFggBEhIJWaZMV51CR0ARkRe/FuFDGkBaGw0RiXZeWhQS" +
            "EgmyvQMpnkJHQBGajD2O+UMaQFodDROJdl5aFggBEhIJsr0DKZ5CR0ARmow9jvlDGkBaGw0TiXZe" +
            "WhQSEgmR/QYAoUJHQBEo5cjd+kMaQFodDRaJdl5aFggBEhIJkf0GAKFCR0ARKOXI3fpDGkBaGw0W" +
            "iXZeWhQSEgnReY1dokJHQBGtPG7lEkQaQFodDRiJdl5aFggBEhIJ0XmNXaJCR0ARrTxu5RJEGkBa" +
            "Gw0YiXZeWhQSEglVl2bNokJHQBEeWHvFQEQaQFodDRuJdl5aFggBEhIJVZdmzaJCR0ARHlh7xUBE" +
            "GkBaGw0biXZeWhQSEgnzT3CxokJHQBHcaABvgUQaQFodDR2Jdl5aFggBEhIJ809wsaJCR0AR3GgA" +
            "b4FEGkBaGw0diXZeWhQSEgkWJlMFo0JHQBEeC/e38UQaQFodDSCJdl5aFggBEhIJFiZTBaNCR0AR" +
            "Hgv3t/FEGkBaGw0giXZeWhQSEgnReY1dokJHQBG+amXCL0UaQFodDSKJdl5aFggBEhIJ0XmNXaJC" +
            "R0ARvmplwi9FGkBaGw0iiXZeWhQSEglvMpdBokJHQBGYDxs/Z0UaQFodDSWJdl5aFggBEhIJbzKX" +
            "QaJCR0ARmA8bP2dFGkBaGw0liXZeWhQSEgl3V2P2n0JHQBHRji5JeEUaQFodDSaJdl5aFggBEhIJ" +
            "d1dj9p9CR0AR0Y4uSXhFGkBaCQ0niXZeUgIIAQ=="

    @Test
    fun fileWithErrorShouldRaiseTOBEDEFINEDException() {
        TODO("Not implemented yet")
    }

    @Test
    fun parsingValidFileShouldGiveCorrectGameID() {
        TODO("Not implemented yet")
    }

    @Test
    fun parsingValidFileShouldGiveCorrectAdminID() {
        TODO("Not implemented yet")
    }

    @Test
    fun parsingValidFileShouldGiveCorrectPlayers() {
        TODO("Not implemented yet")
    }

    @Test
    fun parsingValidFileShouldGiveCorrectBounds() {
        TODO("Not implemented yet")
    }

    @Test
    fun parsingValidFileShouldGiveCorrectEvents() {
        TODO("Not implemented yet")
    }
}