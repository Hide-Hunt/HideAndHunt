package ch.epfl.sdp.user

import ch.epfl.sdp.db.Callback

interface IUserRepo {
    fun getUsername(userID: String, cb: Callback<String>)
    fun addGameToHistory(userID: String, gameID: String)
    fun getGameHistory(userID: String, cb: Callback<List<String>>)
}