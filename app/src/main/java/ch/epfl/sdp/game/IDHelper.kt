package ch.epfl.sdp.game

import ch.epfl.sdp.authentication.LocalUser
import kotlin.math.abs

object IDHelper {

    fun getPlayerID(): Int {
        return abs(LocalUser.uid.hashCode())
    }

}