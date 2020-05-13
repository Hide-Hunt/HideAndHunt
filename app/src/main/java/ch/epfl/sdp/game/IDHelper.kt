package ch.epfl.sdp.game

import ch.epfl.sdp.authentication.LocalUser
import kotlin.math.abs

/**
 * Helper object for mapping user ids to player ids
 */
object IDHelper {

    /**
     * Get the current users's player id
     * @return Int : the current users's player id
     */
    fun getPlayerID(): Int {
        return abs(LocalUser.uid.hashCode())
    }

}