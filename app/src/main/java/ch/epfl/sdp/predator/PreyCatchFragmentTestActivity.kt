package ch.epfl.sdp.predator

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.nfc.Tag
import android.nfc.tech.NfcA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.R
import java.lang.IllegalArgumentException

/**
 * This activity only exists for developing purposes
 */
class PreyCatchFragmentTestActivity : AppCompatActivity() {

    lateinit var pcf: PreyCatchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pcf = PreyCatchFragment()
        pcf.targetTag = "43958fad26480" //Thomas's campipro
        setContentView(R.layout.prey_catch_fragment_test_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, pcf)
                    .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val adapter = NfcAdapter.getDefaultAdapter(this)
        adapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        val adapter = NfcAdapter.getDefaultAdapter(this)
        adapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            pcf.onNfcTagRead(pcf.tagIdToString(tag?.id))
        }
    }


}
