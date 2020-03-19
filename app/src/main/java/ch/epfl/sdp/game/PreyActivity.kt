package ch.epfl.sdp.game

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.databinding.ActivityPreyBinding
import ch.epfl.sdp.game.comm.LocationSynchronizer
import ch.epfl.sdp.game.comm.MQTTRealTimePubSub
import ch.epfl.sdp.game.comm.RealTimePubSub
import ch.epfl.sdp.game.comm.SimpleLocationSynchronizer

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PreyActivity : AppCompatActivity() {


}
