package ch.epfl.sdp.lobby

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.FragmentPlayerParametersBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerParametersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerParametersFragment : Fragment(), View.OnClickListener {

    enum class Faction{
        PREY,
        PREDATOR,
    }
    interface OnFactionChangeListener{
        fun onFactionChange(newFaction: Faction)
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
        binding = FragmentPlayerParametersBinding.inflate(layoutInflater)

        binding.switchFaction.setOnClickListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_parameters, container, false)
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

    override fun onClick(v: View?) {
        if(binding.switchFaction.isChecked)
            listener?.onFactionChange(Faction.PREY)
        else
            listener?.onFactionChange(Faction.PREDATOR)
    }
}
