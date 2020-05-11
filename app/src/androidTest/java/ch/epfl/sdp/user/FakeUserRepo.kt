package ch.epfl.sdp.user

import ch.epfl.sdp.db.Callback

class FakeUserRepo : IUserRepo {
    override fun getUsername(userID: String, cb: Callback<String>) {
        cb(userID+"_username")
    }

    override fun addGameToHistory(userID: String, gameID: String) {
        TODO("Not yet implemented")
    }
}