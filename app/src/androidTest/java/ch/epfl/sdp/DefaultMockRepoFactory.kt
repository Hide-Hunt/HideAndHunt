package ch.epfl.sdp

import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.lobby.global.IGlobalLobbyRepository
import ch.epfl.sdp.replay.IReplayRepository

open class DefaultMockRepoFactory : IRepoFactory {
    override fun makeGlobalLobbyRepository(): IGlobalLobbyRepository {
        TODO("Mock factory method not implemented")
    }

    override fun makeReplayRepository(): IReplayRepository {
        TODO("Not yet implemented")
    }
}