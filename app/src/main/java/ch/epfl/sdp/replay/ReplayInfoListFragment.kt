package ch.epfl.sdp.replay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.databinding.FragmentReplayInfoListBinding
import ch.epfl.sdp.db.IRepoFactory

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ReplayInfoListFragment.OnListFragmentInteractionListener] interface.
 */
class ReplayInfoListFragment : Fragment() {
    private var _binding: FragmentReplayInfoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewAdapter: ReplayInfoRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var repo: IReplayRepository
    var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repo = (it.getSerializable(REPO_FACTORY_ARG) as IRepoFactory).makeReplayRepository()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentReplayInfoListBinding.inflate(inflater)

        repo.getAllGames { games ->
            viewAdapter = ReplayInfoRecyclerViewAdapter(games, listener)
            binding.replayInfoListRecycler.layoutManager = LinearLayoutManager(context)
            binding.replayInfoListRecycler.adapter = viewAdapter
        }

        binding.replayInfoListSwiperefresh.setOnRefreshListener {
            repo.getAllGames { games ->
                viewAdapter.mValues = games
                viewAdapter.notifyDataSetChanged()
                binding.replayInfoListSwiperefresh.isRefreshing = false
            }
        }

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
        fun onListFragmentInteraction(game: ReplayInfo)
    }

    companion object {
        private const val REPO_FACTORY_ARG = "repoFacto"

        @JvmStatic
        fun newInstance(factory: IRepoFactory) =
                ReplayInfoListFragment().apply {
                    val args = Bundle()
                    args.putSerializable(REPO_FACTORY_ARG, factory)
                    this.arguments = args
                }
    }
}
