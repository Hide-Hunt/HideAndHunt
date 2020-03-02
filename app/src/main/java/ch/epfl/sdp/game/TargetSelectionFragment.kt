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
import java.io.Serializable
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [TargetSelectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TargetSelectionFragment : Fragment() {
    interface OnTargetSelectedListener {
        fun onTargetSelected(targetID: Int)
    }

    data class PreyList (val preys: List<Player>)

    class TargetNotFound : IndexOutOfBoundsException()

    private var _binding: FragmentTargetSelectionBinding? = null
    private val binding get() = _binding!!


    private lateinit var targetSelectionDialog: AlertDialog
    private lateinit var listener: OnTargetSelectedListener
    private lateinit var targets: Map<Int, Player>
    var selectedTargetID = 0
    set(value) {
        if (value == NO_TARGET || targets.containsKey(value)) {
            field = value
            updateTargetDisplay()
            listener.onTargetSelected(selectedTargetID)
        } else {
            throw TargetNotFound()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            @Suppress("UNCHECKED_CAST") // TODO Do this in a cleaner way
            val targetList = args.getSerializable(ARG_TARGETS) as List<Player>
            targets = targetList.associateBy { it.id }.toMap()

            val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, targetList)
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Select a target")
                    .setAdapter(adapter) { _, which ->
                        selectedTargetID = targetList[which].id
                        updateTargetDisplay()
                    }
            targetSelectionDialog = builder.create()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        _binding = FragmentTargetSelectionBinding.inflate(inflater, container, false)
        binding.mainFrame.setOnClickListener { targetSelectionDialog.show() }
        selectedTargetID = NO_TARGET
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as OnTargetSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnTargetSelectedListener")
        }
    }

    private fun updateTargetDisplay() {
        if (selectedTargetID == NO_TARGET) {
            binding.currentTarget.setText(R.string.no_target)
        } else {
            binding.currentTarget.text = String.format(Locale.getDefault(), "Player %d", selectedTargetID)
        }
    }

    companion object {
        var NO_TARGET = -1
        private const val ARG_TARGETS = "targets"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param targets List of all selectable targets
         * @return A new instance of fragment TargetSelectionFragment.
         */
        fun <T> newInstance(targets: T): TargetSelectionFragment where T : List<Player?>?, T : Serializable? {
            val fragment = TargetSelectionFragment()
            val args = Bundle()
            args.putSerializable(ARG_TARGETS, targets)
            fragment.arguments = args
            return fragment
        }
    }
}