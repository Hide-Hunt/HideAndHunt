package ch.epfl.sdp

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.databinding.ActivityDebugBinding
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.location.ILocationListener
import ch.epfl.sdp.game.location.LocationHandler

class DebugActivity : AppCompatActivity(), ILocationListener {
    private lateinit var binding: ActivityDebugBinding

    private var mUpdateNb = 0
    private var mPlayerID = -1

    private lateinit var locationHandler: LocationHandler

    override fun onLocationChanged(newLocation: Location) {
        mUpdateNb++
        binding.updateNb.text = mUpdateNb.toString()
        binding.location.text = String.format("%s,  %s", newLocation.latitude, newLocation.longitude)
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

    override fun onPlayerLocationUpdate(playerId: String, location: Location) {
    }

    override fun onPreyCatches(predatorId: String, preyId: String) {
    }


    private fun initializeTracking() {
        if (locationHandler.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            binding.GPSDisabledLabel.visibility = View.INVISIBLE
        } else {
            binding.GPSDisabledLabel.visibility = View.VISIBLE
        }
        binding.tracking.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.playerID.isEnabled = false
                locationHandler.enableRequestUpdates()
            } else {
                binding.playerID.isEnabled = true
                locationHandler.removeUpdates()
            }
        }
    }

    private fun initializeLastLocationOnClickListener() {
        binding.repeatLastLocation.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return@OnClickListener
            }
            val last = locationHandler.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (last != null) {
                locationHandler.locationSynchronizer.updateOwnLocation(Location(last.latitude, last.longitude))
            } else {
                Toast.makeText(applicationContext, "No last known location", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initializePlayerIDTextChangedListener() {
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationHandler = LocationHandler(this, this, 0, "0", null)

        initializeTracking()
        initializePlayerIDTextChangedListener()
        initializeLastLocationOnClickListener()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        locationHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
