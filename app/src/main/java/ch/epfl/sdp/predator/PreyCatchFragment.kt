package ch.epfl.sdp.predator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ch.epfl.sdp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PreyCatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreyCatchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
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
