package ch.epfl.sdp.game

import android.content.Intent
import ch.epfl.sdp.game.data.Player
import java.lang.IllegalArgumentException

class GameIntentUnpacker {

    companion object {
        fun unpack(intent: Intent): GameIntentData {

            val gameID = intent.getIntExtra("gameID", -1)
            val playerID = intent.getIntExtra("playerID", -1)
            val initialTime = intent.getLongExtra("initialTime", -1)

            val playerList = if(intent.getSerializableExtra("players") == null) emptyList() else (intent.getSerializableExtra("players") as List<*>).filterIsInstance<Player>()
            val mqttURI = intent.getStringExtra("mqttURI")

            return GameIntentData(gameID, playerID, initialTime, playerList, mqttURI)
        }
    }

    data class GameIntentData(val gameID: Int, val playerID: Int, val initialTime: Long, val playerList: List<Player>, val mqttURI: String?)

}