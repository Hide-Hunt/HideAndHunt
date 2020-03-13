package ch.epfl.sdp.lobby.global

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.databinding.FragmentGlobalLobbyBinding

class GlobalLobbyFragment : Fragment() {

    private var _binding: FragmentGlobalLobbyBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var repo: IGlobalLobbyRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGlobalLobbyBinding.inflate(inflater)
        repo?.getAllGames { games ->
            viewManager = LinearLayoutManager(context)
            viewAdapter = GlobalLobbyAdapter(games)
            binding.globalLobbyRecycler.apply {
                layoutManager = viewManager
                adapter = viewAdapter
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
        fun newInstance() = GlobalLobbyFragment().apply {}
    }
}
