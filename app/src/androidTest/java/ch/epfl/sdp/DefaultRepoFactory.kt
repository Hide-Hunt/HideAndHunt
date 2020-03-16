package ch.epfl.sdp

import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository

open class DefaultRepoFactory : IRepoFactory {
    override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
        TODO("Mock factory method not implemented")
    }
}