package ch.epfl.sdp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.databinding.ActivityDebugBinding
import ch.epfl.sdp.databinding.ActivityPreyBinding
import ch.epfl.sdp.game.comm.LocationSynchronizer
import ch.epfl.sdp.game.comm.MQTTRealTimePubSub
import ch.epfl.sdp.game.comm.RealTimePubSub
import ch.epfl.sdp.game.comm.SimpleLocationSynchronizer

class DebugActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDebugBinding

    private lateinit var mLocationManager: LocationManager
    private var mUpdateNb = 0
    private var mPlayerID = -1

    private lateinit var pubSub: RealTimePubSub
    private lateinit var locationSynchronizer: LocationSynchronizer

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            mUpdateNb++
            binding.updateNb.text = mUpdateNb.toString()
            binding.location.text = String.format("%s,  %s", location.latitude, location.longitude)
            locationSynchronizer.updateOwnLocation(ch.epfl.sdp.game.data.Location(location.latitude, location.longitude))
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Toast.makeText(applicationContext, "onStatusChanged: $status", Toast.LENGTH_LONG).show()
        }

        override fun onProviderEnabled(provider: String) {
            binding.GPSDisabledLabel.visibility = View.INVISIBLE
        }

        override fun onProviderDisabled(provider: String) {
            binding.GPSDisabledLabel.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pubSub = MQTTRealTimePubSub(this, null)
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            binding.GPSDisabledLabel.visibility = View.INVISIBLE
        } else {
            binding.GPSDisabledLabel.visibility = View.VISIBLE
        }
        binding.tracking.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.playerID.isEnabled = false
                enableRequestUpdates()
            } else {
                binding.playerID.isEnabled = true
                disableRequestUpdates()
            }
        }
        binding.playerID.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    mPlayerID = s.toString().toInt()
                    binding.tracking.isEnabled = s.isNotEmpty()
                } catch (ignored: NumberFormatException) {
                    binding.tracking.isEnabled = false
                }
            }
        })
        binding.repeatLastLocation.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return@OnClickListener
            }
            val last = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (last != null) {
                locationSynchronizer.updateOwnLocation(ch.epfl.sdp.game.data.Location(last.latitude, last.longitude))
            } else {
                Toast.makeText(applicationContext, "No last known location", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            enableRequestUpdates()
        }
    }

    private fun enableRequestUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                return
            }
        }
        locationSynchronizer = SimpleLocationSynchronizer(0, mPlayerID, pubSub)
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(), LOCATION_REFRESH_DISTANCE.toFloat(), mLocationListener)
    }

    private fun disableRequestUpdates() {
        mLocationManager.removeUpdates(mLocationListener)
    }

    companion object {
        private const val LOCATION_REFRESH_DISTANCE = 5
        private const val LOCATION_REFRESH_TIME = 1000
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10
    }
}
