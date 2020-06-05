package ch.epfl.sdp.user

import ch.epfl.sdp.db.Callback

class FakeUserRepo : IUserRepo {
    private var gameHistory = HashMap<String, MutableList<String>>()

    override fun getUsername(userID: String, cb: Callback<String>) {
        cb(userID+"_username")
    }

    override fun addGameToHistory(userID: String, gameID: String) {
        gameHistory[userID]?.add(gameID)
    }

    override fun getGameHistory(userID: String, cb: Callback<List<String>>) {
        cb(gameHistory[userID].orEmpty())
    }
}