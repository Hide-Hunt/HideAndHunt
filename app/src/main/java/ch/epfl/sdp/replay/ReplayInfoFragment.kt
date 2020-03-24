package ch.epfl.sdp.replay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.databinding.FragmentReplayInfoBinding
import ch.epfl.sdp.db.IRepoFactory

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ReplayInfoFragment.OnListFragmentInteractionListener] interface.
 */
class ReplayInfoFragment : Fragment() {
    private var _binding: FragmentReplayInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewAdapter: ReplayInfoRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var repo: IReplayRepository
    var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repo = (it.getSerializable(REPO_FACTORY_ARG) as IRepoFactory).makeReplyRepository()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentReplayInfoBinding.inflate(inflater)

        viewManager = LinearLayoutManager(context)
        viewAdapter = ReplayInfoRecyclerViewAdapter(listOf(), listener)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(gameID: Int)
    }

    companion object {
        private const val REPO_FACTORY_ARG = "repoFacto"

        @JvmStatic
        fun newInstance(factory: IRepoFactory) =
                ReplayInfoFragment().apply {
                    val args = Bundle()
                    args.putSerializable(REPO_FACTORY_ARG, factory)
                    this.arguments = args
                }
    }
}
