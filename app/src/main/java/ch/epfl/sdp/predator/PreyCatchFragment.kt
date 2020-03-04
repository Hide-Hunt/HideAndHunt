package ch.epfl.sdp.predator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.ActivityLobbyBinding
import ch.epfl.sdp.databinding.FragmentPreyCatchBinding

class PreyCatchFragment : Fragment() {

    var targetTag: String = ""
    private var preyCatchBinding: FragmentPreyCatchBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        preyCatchBinding = FragmentPreyCatchBinding.inflate(inflater)
        return preyCatchBinding?.root
    }

    fun onNfcTagRead(tagID: String) {
        if(tagID != "" && tagID == targetTag) {
            val txt = preyCatchBinding?.preyState
            txt?.setText(R.string.prey_dead)
        }
    }

}
