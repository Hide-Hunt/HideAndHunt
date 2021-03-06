package ch.epfl.sdp.lobby.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.FragmentGlobalLobbyBinding
import ch.epfl.sdp.user.IUserRepo
import javax.inject.Inject

/**
 * A [Fragment] showing a list of all available games
 */
class GlobalLobbyFragment : Fragment() {
    private var _binding: FragmentGlobalLobbyBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewAdapter: GlobalLobbyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    @Inject lateinit var repo: IGlobalLobbyRepository
    @Inject lateinit var userRepo: IUserRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        // We have to handle the dependency injection before the call to super.onCreate
        (activity?.applicationContext as HideAndHuntApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGlobalLobbyBinding.inflate(inflater)
        repo.getAllGames { games ->
            viewManager = LinearLayoutManager(context)
            viewAdapter = GlobalLobbyAdapter(games, userRepo)
            binding.globalLobbyRecycler.apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }

        binding.globalLobbySwiperefresh.setOnRefreshListener {
            repo.getAllGames { games ->
                viewAdapter.data = games
                viewAdapter.notifyDataSetChanged()
                binding.globalLobbySwiperefresh.isRefreshing = false
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = GlobalLobbyFragment().apply{}
    }
}
