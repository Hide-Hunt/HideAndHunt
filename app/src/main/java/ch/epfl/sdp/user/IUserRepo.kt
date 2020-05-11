package ch.epfl.sdp.user

import ch.epfl.sdp.db.Callback

interface IUserRepo {
    fun getUsername(userID: String, cb: Callback<String>)
    fun addGameToHistory(userID: String, gameID: String)
}