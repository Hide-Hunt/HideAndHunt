package ch.epfl.sdp.lobby

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.epfl.sdp.databinding.FragmentPlayerParametersBinding
import ch.epfl.sdp.game.PlayerFaction

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerParametersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerParametersFragment : Fragment() {

    interface OnFactionChangeListener {
        fun onFactionChange(newFaction: PlayerFaction)
    }

    var listener: OnFactionChangeListener? = null
    private lateinit var binding: FragmentPlayerParametersBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFactionChangeListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Check whether the required parameters are in there
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentPlayerParametersBinding.inflate(inflater)

        binding.switchFaction.setOnClickListener {
            if (binding.switchFaction.isChecked) {
                listener?.onFactionChange(PlayerFaction.PREY)
            } else {
                listener?.onFactionChange(PlayerFaction.PREDATOR)
            }
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PlayerParametersFragment.
         */
        @JvmStatic
        fun newInstance() =
                PlayerParametersFragment().apply {
                    arguments = Bundle().apply {
                        //Add required parameters
                    }
                }
    }

}
