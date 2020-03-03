package ch.epfl.sdp.predator

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ch.epfl.sdp.R
import kotlinx.android.synthetic.main.prey_catch_fragment_test_activity.*

class PreyCatchFragment : Fragment() {

    var targetTag: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_prey_catch, container, false)
    }

    fun onNfcTagRead(tagID: String) {
        if(tagID != "" && tagID == targetTag) {
            val txt = view?.findViewById<TextView>(R.id.prey_state)
            txt?.clearComposingText()
            txt?.setText(R.string.prey_dead)
        }
    }

}
