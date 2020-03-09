package ch.epfl.sdp.db

import ch.epfl.sdp.game.data.Game

typealias Callback<T> = (T) -> Unit

interface DB {
    fun addGame(game: Game, callback: Callback<Boolean>)
    fun getAllGames(callback: Callback<List<Game>>)
}