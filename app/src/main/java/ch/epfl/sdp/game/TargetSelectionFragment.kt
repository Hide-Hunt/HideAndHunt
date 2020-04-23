package ch.epfl.sdp.game

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.FragmentTargetSelectionBinding
import ch.epfl.sdp.game.data.Player
import java.io.Serializable
import java.util.*

/**
 * A player selection fragment.
 * Use the [TargetSelectionFragment.newInstance] factory method to create an instance of this fragment.
 */
class TargetSelectionFragment : Fragment() {
    interface OnTargetSelectedListener {
        fun onTargetSelected(targetID: String)
    }

    private var _binding: FragmentTargetSelectionBinding? = null
    private val binding get() = _binding!!

    private var targetSelectionDialog: AlertDialog? = null
    var listener: OnTargetSelectedListener? = null

    // TODO use a ViewModel / Model to share this state with activity and other models
    private lateinit var targets: Map<String, Player>

    var selectedTargetID = ""
        set(value) {
            if (value == NO_TARGET || targets.containsKey(value)) {
                field = value
                updateTargetDisplay()
                listener?.onTargetSelected(selectedTargetID)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val targetList = if (arguments != null) {
            @Suppress("UNCHECKED_CAST")
            arguments!!.getSerializable(ARG_TARGETS) as List<Player>
        } else {
            ArrayList()
        }

        targets = targetList.associateBy { it.id }.toMap()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, targetList)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.target_selection_dialog_title))
                .setAdapter(adapter) { _, which ->
                    selectedTargetID = targetList[which].id
                    updateTargetDisplay()
                }
        targetSelectionDialog = builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        _binding = FragmentTargetSelectionBinding.inflate(inflater, container, false)

        binding.targetSelectionMainLayout.setOnClickListener {
            targetSelectionDialog?.show()
        }

        selectedTargetID = savedInstanceState?.getString(ARG_SELECTED_TARGET_ID) ?: NO_TARGET

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.targetSelectionMainLayout.setOnClickListener(null)
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTargetSelectedListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_SELECTED_TARGET_ID, selectedTargetID)
    }

    private fun updateTargetDisplay() {
        if (selectedTargetID == NO_TARGET) {
            binding.currentTarget.setText(R.string.no_target)
        } else {
            binding.currentTarget.text = String.format(Locale.getDefault(), "Player %s", selectedTargetID)
        }
    }

    companion object {
        const val NO_TARGET = "-1"
        private const val ARG_TARGETS = "targets"
        private const val ARG_SELECTED_TARGET_ID = "selectedTargetID"
        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @param targets List of all selectable targets
         * @return A new instance of fragment TargetSelectionFragment.
         */
        fun <T> newInstance(targets: T): TargetSelectionFragment where T : List<Player>, T : Serializable {
            val fragment = TargetSelectionFragment()
            val args = Bundle()
            args.putSerializable(ARG_TARGETS, targets)
            fragment.arguments = args
            return fragment
        }
    }
}