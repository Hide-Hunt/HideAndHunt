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

class LocationHandler(val activity: AppCompatActivity, val listener: ILocationListener, val gameID: Int, val playerID: Int, val URI: String?) {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10
        private const val LOCATION_REFRESH_TIME = 1000
        private const val LOCATION_REFRESH_DISTANCE = 5
    }

    private val context = activity.applicationContext
    val lastKnownLocation: Location = Location(0.0, 0.0)
    private val locationSynchronizer: LocationSynchronizer =  SimpleLocationSynchronizer(gameID, playerID, MQTTRealTimePubSub(context, URI))
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

    fun unsubscribeFromPlayer(playerID: Int) {
        locationSynchronizer.unsubscribeFromPlayer(playerID)
    }

    fun subscribeToPlayer(playerID: Int) {
        locationSynchronizer.subscribeToPlayer(playerID)
    }

    fun declareCatch(preyID: Int) {
        locationSynchronizer.declareCatch(preyID)
    }

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

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            enableRequestUpdates()
        }
    }

    fun removeUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    fun stop() {
        locationSynchronizer.stop()
    }

}