package ch.epfl.sdp.utils

import ch.epfl.sdp.game.comm.GameEventOuterClass
import ch.epfl.sdp.game.comm.GameOuterClass
import ch.epfl.sdp.game.data.Player
import ch.epfl.sdp.replay.game_history.GameEvent
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class ProtobufExtensionsTest {
    @get:Rule
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun protoToGameEventShouldThrowWrongGameEventFormatForUnknownEventType() {
        val gameEvent = GameEventOuterClass.GameEvent.newBuilder().build()
        exception.expect(GameEvent.WrongGameEventFormat::class.java)
        gameEvent.protoToGameEvent()
    }

    @Test
    fun protoToPlayerShouldThrowWrongGameEventFormatForUnknownEventType() {
        val player = GameOuterClass.Player.newBuilder()
                .setFaction(GameOuterClass.Faction.UNRECOGNIZED)
                .build()
        exception.expect(Player.WrongPlayerFormat::class.java)
        player.protoToPlayer()
    }
}