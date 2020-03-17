package ch.epfl.sdp.db

import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import java.io.Serializable

/**
 * Factory that creates all repositories
 * This interface contains all methods to create the repositories needed in this project and their link to the DB.
 */
interface IRepoFactory : Serializable {

    fun makeGlobalLobbyRepository(): IGlobalLobbyRepository

}