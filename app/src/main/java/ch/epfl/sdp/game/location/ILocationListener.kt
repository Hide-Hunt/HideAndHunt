package ch.epfl.sdp.game.location

import android.os.Bundle
import ch.epfl.sdp.game.data.Location

interface ILocationListener {

    /**
     * Function called when the player's location change
     * @param newLocation Location: The new [Location] of the player
     */
    fun onLocationChanged(newLocation: Location)

    /**
     * Function called when the player's status changed
     * @param provider String: String describing the author of the action
     * @param status Int: The status of the player
     * @param extras Bundle: [Bundle] for additional data
     */
    fun onStatusChanged(provider: String, status: Int, extras: Bundle)

    /**
     * Function called when tracking is enabled
     * @param provider String: String describing the author of the action
     */
    fun onProviderEnabled(provider: String)

    /**
     * Function called when tracking is disabled
     * @param provider String: String describing the author of the action
     */
    fun onProviderDisabled(provider: String)

    /**
     * Function called when a new location for the player is available
     * @param playerID Int: the player ID
     * @param location Location: the new [Location] of the player
     */
    fun onPlayerLocationUpdate(playerID: Int, location: Location)

    /**
     * Function called when a prey is catched
     * @param predatorID Int: the predator (catcher) ID
     * @param preyID Int: the prey (catched) ID
     */
    fun onPreyCatches(predatorID: Int, preyID: Int)
}