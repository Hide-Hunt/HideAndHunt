package ch.epfl.sdp.game

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.databinding.ActivityPredatorBinding
import ch.epfl.sdp.game.TargetSelectionFragment.Companion.newInstance
import ch.epfl.sdp.game.TargetSelectionFragment.OnTargetSelectedListener
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Location
import ch.epfl.sdp.game.data.Player


class PredatorActivity : AppCompatActivity(), OnTargetSelectedListener {
    private lateinit var mLocationManager: LocationManager
    private var _binding: ActivityPredatorBinding? = null
    private val binding get() = _binding!!

    private var gameID = -1
    private var playerID = -1
    private var targetID = -1

    private val lastKnownLocation: Location = Location(0.0, 0.0)
    private var players = HashMap<Int, Player>()
    private var preys = HashMap<String, Int>()
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
            if(!loadIntentPayload(intent)) {
                finish()
                return
            }
        }

        loadFragments()

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        binding.distanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (targetID != TargetSelectionFragment.NO_TARGET) targetSDistanceFragment.distance = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }
    private fun loadIntentPayload(intent: Intent) : Boolean {
        gameID = intent.getIntExtra("gameID", -1)
        playerID = intent.getIntExtra("playerID", -1)

        if (gameID < 0 || playerID < 0) {
            return false
        }

        @Suppress("UNCHECKED_CAST")
        try {
            val playerList = intent.getSerializableExtra("players") as ArrayList<Player>
            for (p in playerList) {
                players[p.id] = p
                if (p.faction ==  Faction.PREY) {
                    preys[p.NFCTag] = p.id
                }
            }
        } catch (e: NullPointerException) {
            return false
        }


        return true
    }

    private fun loadFragments() { // create a FragmentManager
        val fm = supportFragmentManager
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()
        // replace the FrameLayout with new Fragment
        targetSelectionFragment = newInstance(ArrayList(players.values.toList()))
        fragmentTransaction.add(binding.targetSelectionPlaceHolder.id, targetSelectionFragment)

        targetSDistanceFragment = TargetDistanceFragment.newInstance(arrayListOf(0,10,25,50,75))
        fragmentTransaction.add(binding.targetDistancePlaceHolder.id, targetSDistanceFragment)

        fragmentTransaction.commit() // save the changes
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val adapter = NfcAdapter.getDefaultAdapter(this)
        adapter?.enableForegroundDispatch(this, pendingIntent, null, null)
        enableRequestUpdates()
    }

    override fun onTargetSelected(targetID: Int) {
        this.targetID = targetID
        if (targetID != TargetSelectionFragment.NO_TARGET) {
            players[targetID]?.lastKnownLocation = null
            targetSDistanceFragment.distance = TargetDistanceFragment.NO_DISTANCE
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            NFCTagHelper.intentToNFCTag(intent)?.let {
                preys[it]?.let { preyID ->
                    onPreyCatch(preyID)
                }
            }
        }
    }

    private fun onPreyCatch(preyID: Int) {
        Toast.makeText(this, "Catched a prey : id=" + players[preyID]?.id, Toast.LENGTH_LONG).show()
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

    private fun disableRequestUpdates() {
        mLocationManager.removeUpdates(mLocationListener)
    }

    override fun onPause() {
        super.onPause()
        disableRequestUpdates()
        NfcAdapter.getDefaultAdapter(this)?.disableForegroundDispatch(this)
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10
        private const val LOCATION_REFRESH_TIME = 1000
        private const val LOCATION_REFRESH_DISTANCE = 5
    }
}