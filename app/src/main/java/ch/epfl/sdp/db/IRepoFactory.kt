package ch.epfl.sdp.db

import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import java.io.Serializable

interface IRepoFactory : Serializable {

    fun makeGlobalLobbyRepository(): IGlobalLobbyRepository

}