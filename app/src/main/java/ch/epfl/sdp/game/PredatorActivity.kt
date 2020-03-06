package ch.epfl.sdp.game

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.databinding.ActivityPredatorBinding
import ch.epfl.sdp.game.TargetSelectionFragment.Companion.newInstance
import ch.epfl.sdp.game.TargetSelectionFragment.OnTargetSelectedListener

class PredatorActivity : AppCompatActivity(), OnTargetSelectedListener {
    private lateinit var mLocationManager: LocationManager
    private var _binding: ActivityPredatorBinding? = null
    private val binding get() = _binding!!

    private var gameID = 0
    private var player: Player? = null
    private var targetID = 0
    private val lastKnownLocation: Location = Location(0.0,0.0)
    private lateinit var players: HashMap<Int, Player>
    private lateinit var targetSelectionFragment: TargetSelectionFragment
    private lateinit var targetSDistanceFragment: TargetDistanceFragment

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location) {
            lastKnownLocation.latitude = location.latitude
            lastKnownLocation.longitude = location.longitude
            if (targetID != TargetSelectionFragment.NO_TARGET) {
                targetSDistanceFragment.distance =
                        players[targetID]?.lastKnownLocation?.distanceTo(lastKnownLocation)?.toInt() ?: TargetDistanceFragment.NO_DISTANCE
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Toast.makeText(applicationContext, "Location: onStatusChanged: $status", Toast.LENGTH_LONG).show()
        }

        override fun onProviderEnabled(provider: String) {
            if (targetID != TargetSelectionFragment.NO_TARGET) {
                targetSDistanceFragment.distance = TargetDistanceFragment.NO_DISTANCE
            }
        }

        override fun onProviderDisabled(provider: String) {
            targetSDistanceFragment.distance = TargetDistanceFragment.DISABLED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPredatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get game information
        if (savedInstanceState == null) { // First load
            loadIntentPayload(intent)
            loadFragments()
        } /* TODO get those information from bundle
        else { // Restore state from previous activity
            loadBundle(savedInstanceState)
        } */

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        enableRequestUpdates()

        binding.distanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (targetID != TargetSelectionFragment.NO_TARGET) targetSDistanceFragment.distance = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }

    private fun loadIntentPayload(intent: Intent) {
        // TODO get those information from intent
        fakeLoad()
    }

    /*
    private fun loadBundle(bundle: Bundle) {
    // TODO get those information from bundle
        fakeLoad()
    }
    */

    private fun loadFragments() { // create a FragmentManager
        val fm = supportFragmentManager
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()
        // replace the FrameLayout with new Fragment
        targetSelectionFragment = newInstance(ArrayList(players.values.toList()))
        fragmentTransaction.replace(binding.targetSelectionPlaceHolder.id, targetSelectionFragment)

        targetSDistanceFragment = TargetDistanceFragment.newInstance(arrayListOf(0,10,25,50,75))
        fragmentTransaction.replace(binding.targetDistancePlaceHolder.id, targetSDistanceFragment)

        fragmentTransaction.commit() // save the changes
    }

    private fun fakeLoad() {
        gameID = 0
        targetID = TargetSelectionFragment.NO_TARGET
        player = Player(0, Faction.PREDATOR)
        players = HashMap()
        for (i in 1..9) {
            val faction = if (Math.random() % 2 == 0.0) Faction.PREDATOR else Faction.PREY
            players[i] = Player(i, faction)
        }
    }

    override fun onTargetSelected(targetID: Int) {
        this.targetID = targetID
        if (targetID != TargetSelectionFragment.NO_TARGET) {
            players[targetID]?.lastKnownLocation = null
            targetSDistanceFragment.distance = TargetDistanceFragment.NO_DISTANCE
        }
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
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(), LOCATION_REFRESH_DISTANCE.toFloat(), mLocationListener)
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10
        private const val LOCATION_REFRESH_TIME = 1000
        private const val LOCATION_REFRESH_DISTANCE = 5
    }
}