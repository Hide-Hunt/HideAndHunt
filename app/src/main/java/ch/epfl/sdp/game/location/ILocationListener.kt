package ch.epfl.sdp.game.location

import android.os.Bundle
import ch.epfl.sdp.game.data.Location

interface ILocationListener {

    fun onLocationChanged(newLocation: Location)
    fun onStatusChanged(provider: String, status: Int, extras: Bundle)
    fun onProviderEnabled(provider: String)
    fun onProviderDisabled(provider: String)

    fun onPlayerLocationUpdate(playerID: Int, location: Location)
    fun onPreyCatches(predatorID: Int, preyID: Int)
}