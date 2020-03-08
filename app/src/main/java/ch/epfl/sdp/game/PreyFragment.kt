package ch.epfl.sdp.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Prey

/**
 * A fragment representing a list of Items.
 */
class PreyFragment : Fragment() {
    // TODO use a ViewModel / Model to share this state with activity and other models
    private lateinit var preys: ArrayList<Prey>
    private lateinit var viewAdapter: PreyRecyclerViewAdapter

    private val comparator = Comparator<Prey> { o1, o2 ->
        when {
            o1.state == Prey.PreyState.DEAD && o2.state == Prey.PreyState.ALIVE -> 1
            o1.state == o2.state && o1.id > o2.id -> 1
            else -> -1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            preys = it.getSerializable(ARG_PREYS) as ArrayList<Prey>
            preys.sortWith(comparator)
        }
        if (arguments == null) {
            preys = ArrayList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_prey_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                viewAdapter = PreyRecyclerViewAdapter(preys)
                adapter = viewAdapter
            }
        }
        return view
    }

    fun setPreyState(preyID: Int, state: Prey.PreyState) {
        viewAdapter.mValues.first { it.id == preyID }.state = state
        viewAdapter.mValues.sortWith(comparator)
        viewAdapter.notifyDataSetChanged()
    }

    companion object {
        const val ARG_PREYS = "preys"

        @JvmStatic
        fun newInstance(preys: ArrayList<Prey>) =
                PreyFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PREYS, preys)
                    }
                }
    }
}
