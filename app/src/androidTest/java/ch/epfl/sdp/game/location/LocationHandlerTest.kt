package ch.epfl.sdp.game.location

import android.content.Intent
import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.DebugActivity
import ch.epfl.sdp.game.comm.SimpleLocationSynchronizer
import ch.epfl.sdp.game.data.Location
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LocationHandlerTest {

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule @JvmField
    val activityRule = ActivityTestRule(DebugActivity::class.java, false, false)

    private fun makeListener(anyCalled: (String) -> Unit): ILocationListener = object : ILocationListener {
        override fun onLocationChanged(newLocation: Location) {
            anyCalled("location")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            anyCalled("status")
        }

        override fun onProviderEnabled(provider: String) {
            anyCalled("provEnabled")
        }

        override fun onProviderDisabled(provider: String) {
            anyCalled("provDisabled")
        }

        override fun onPlayerLocationUpdate(playerID: Int, location: Location) {
            anyCalled("playerUpdate")
        }

        override fun onPreyCatches(predatorID: Int, preyID: Int) {
            anyCalled("preyCatch")
        }
    }

    private fun createMockLocation(lat: Double, lon: Double): android.location.Location {
        val mockLocation = android.location.Location("mock_test_gps")
        mockLocation.latitude = lat
        mockLocation.longitude = lon
        mockLocation.altitude = 0.0
        mockLocation.time = System.currentTimeMillis()
        mockLocation.accuracy = 5f
        mockLocation.elapsedRealtimeNanos = 1L
        return mockLocation
    }

    @Test
    fun locationChangeIsApplied() {
        var called = false
        val listener = makeListener { func ->
            if(func == "location") {
                called = true
            }
        }
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activity = activityRule.launchActivity(intent)
        val handler = LocationHandler(activity, listener, 0, 0, null)
        handler.locationListener.onLocationChanged(createMockLocation(10.0, 11.0))
        assertEquals(10.0, handler.lastKnownLocation.latitude, 0.1)
        assertEquals(11.0, handler.lastKnownLocation.longitude, 0.1)
        assertTrue(called)
    }

    @Test
    fun providerEnableIsApplied() {
        var called = false
        val listener = makeListener { func ->
            if(func == "provEnabled") {
                called = true
            }
        }
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activity = activityRule.launchActivity(intent)
        val handler = LocationHandler(activity, listener, 0, 0, null)
        handler.locationListener.onProviderEnabled("")
        assertTrue(called)

    }

    @Test
    fun providerDisableIsApplied() {
        var called = false
        val listener = makeListener { func ->
            if(func == "status") {
                called = true
            }
        }
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activity = activityRule.launchActivity(intent)
        val handler = LocationHandler(activity, listener, 0, 0, null)
        handler.locationListener.onStatusChanged("gps", 0, Bundle())
        assertTrue(called)
    }

    @Test
    fun statusChangeIsApplied() {
        var called = false
        val listener = makeListener { func ->
            if(func == "provDisabled") {
                called = true
            }
        }
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activity = activityRule.launchActivity(intent)
        val handler = LocationHandler(activity, listener, 0, 0, null)
        handler.locationListener.onProviderDisabled("")
        assertTrue(called)
    }

    @Test
    fun enableRequestIsApplied() {
        val intent = Intent()
        val listener = makeListener { Unit }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activity = activityRule.launchActivity(intent)
        val handler = LocationHandler(activity, listener, 0, 0, null)
        activity.runOnUiThread {
            handler.enableRequestUpdates()
            handler.onRequestPermissionsResult(10, arrayOf(""), IntArray(0))
        }
        assertFalse(activity.isDestroyed)
    }

    @Test
    fun stopIsApplied() {
        val intent = Intent()
        val listener = makeListener { Unit }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activity = activityRule.launchActivity(intent)
        val handler = LocationHandler(activity, listener, 0, 0, null)
        assertNotEquals(null, (handler.locationSynchronizer as SimpleLocationSynchronizer).listener)
        handler.stop()
        assertEquals(null, (handler.locationSynchronizer as SimpleLocationSynchronizer).listener)
    }

}