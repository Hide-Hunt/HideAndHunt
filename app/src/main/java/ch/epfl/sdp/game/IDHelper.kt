package ch.epfl.sdp.game

import ch.epfl.sdp.authentication.LocalUser

object IDHelper {

    fun getPlayerID(): Int {
        return LocalUser.uid.hashCode()
    }

}