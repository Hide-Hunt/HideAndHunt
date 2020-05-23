package ch.epfl.sdp.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter

object NFCHelper {
    fun enableForegroundDispatch(activity: Activity, cls: Class<*>) {
        val intent = Intent(activity, cls).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0)
        val adapter = NfcAdapter.getDefaultAdapter(activity)
        adapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
    }

    fun disableForegroundDispatch(activity: Activity) {
        NfcAdapter.getDefaultAdapter(activity)?.disableForegroundDispatch(activity)
    }
}