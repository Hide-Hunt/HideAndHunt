package ch.epfl.sdp.predator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ch.epfl.sdp.R
class PreyCatchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_prey_catch, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PreyCatchFragment().apply {
                /*arguments = Bundle().apply {
                   putString(ARG_PARAM1, param1)
                   putString(ARG_PARAM2, param2)
               }*/
            }

    }
}
