package ch.epfl.sdp.game.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.game.comm.LocationSynchronizer
import ch.epfl.sdp.game.comm.MQTTRealTimePubSub
import ch.epfl.sdp.game.comm.SimpleLocationSynchronizer
import ch.epfl.sdp.game.data.Location

/**
 * Handles the location processing for an [AppCompatActivity] and a game
 * @param activity AppCompatActivity: The Activity on which the location tracking must be enables
 * @param listener ILocationListener: An [ILocationListener] to listen to updates
 * @param gameID Int: The game ID
 * @param playerID Int: The current player's ID
 * @param URI String: MQTT server URI
 */
class LocationHandler(val activity: AppCompatActivity, val listener: ILocationListener, val gameID: String, val playerID: Int, private val URI: String?) {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10
        private const val LOCATION_REFRESH_TIME = 1000
        private const val LOCATION_REFRESH_DISTANCE = 5
    }

    private val context = activity.applicationContext
    val lastKnownLocation: Location = Location(0.0, 0.0)
    val locationSynchronizer: LocationSynchronizer = SimpleLocationSynchronizer(gameID, playerID, MQTTRealTimePubSub(context, URI))
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location) {
            lastKnownLocation.latitude = location.latitude
            lastKnownLocation.longitude = location.longitude
            locationSynchronizer.updateOwnLocation(lastKnownLocation)
            listener.onLocationChanged(lastKnownLocation)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            listener.onStatusChanged(provider, status, extras)
        }

        override fun onProviderEnabled(provider: String) {
            listener.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            listener.onProviderDisabled(provider)
        }
    }

    init {
        locationSynchronizer.setPlayerUpdateListener(object : LocationSynchronizer.PlayerUpdateListener {
            override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
                listener.onPlayerLocationUpdate(playerID, location)
            }

            override fun onPreyCatches(predatorID: Int, preyID: Int) {
                listener.onPreyCatches(predatorID, preyID)
            }
        })
    }

    /**
     * Unsubscribe a player to the location updates
     * @param playerID Int: The player's ID
     */
    fun unsubscribeFromPlayer(playerID: Int) {
        locationSynchronizer.unsubscribeFromPlayer(playerID)
    }

    /**
     * Subscribe a player to the location updates
     * @param playerID Int: The player's ID
     */
    fun subscribeToPlayer(playerID: Int) {
        locationSynchronizer.subscribeToPlayer(playerID)
    }

    fun emitLocation() {
        locationSynchronizer.updateOwnLocation(lastKnownLocation)
    }

    fun declareCatch(preyID: Int) {
        locationSynchronizer.declareCatch(preyID)
    }

    /**
     * Enable the context authorization to get GPS updates
     */
    fun enableRequestUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                return
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(), LOCATION_REFRESH_DISTANCE.toFloat(), locationListener)
    }

    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            enableRequestUpdates()
        }
    }

    /**
     * Remove the context authorization to get GPS updates
     */
    fun removeUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    /**
     * Stop the location updates
     */
    fun stop() {
        locationSynchronizer.stop()
    }

}