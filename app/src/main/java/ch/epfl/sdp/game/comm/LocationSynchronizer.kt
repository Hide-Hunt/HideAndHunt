package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location

/**
 * Interface providing required methods for real time communication about players and locations
 */
interface LocationSynchronizer {
    /**
     * Interface providing required methods for real time player information
     */
    interface PlayerUpdateListener {
        /**
         * Executed when a player moves
         * @param playerID Int: The ID of the moving player
         * @param location Location: The new [Location] of the player
         */
        fun onPlayerLocationUpdate(playerID: Int, location: Location)

        /**
         * Executed when a prey is caught
         * @param predatorID Int: The ID of the predator who caught
         * @param preyID Int: The ID of the caught prey
         */
        fun onPreyCatches(predatorID: Int, preyID: Int)
    }

    /**
     * Executed when the current player moves
     * @param location Location: The new player [Location]
     */
    fun updateOwnLocation(location: Location)

    /**
     * Executed when the current player caught a prey
     * @param playerID Int: The ID of the caught prey
     */
    fun declareCatch(playerID: Int)

    /**
     * Subscribe to a given player
     * @param playerID Int: The ID of the player
     */
    fun subscribeToPlayer(playerID: Int)

    /**
     * Unsubscribe to a given player
     * @param playerID Int: The ID of the player
     */
    fun unsubscribeFromPlayer(playerID: Int)

    /**
     * Define a new [PlayerUpdateListener] for the current player
     * @param listener PlayerUpdateListener: [PlayerUpdateListener]
     */
    fun setPlayerUpdateListener(listener: PlayerUpdateListener)

    /**
     * Stop listening
     */
    fun stop()
}