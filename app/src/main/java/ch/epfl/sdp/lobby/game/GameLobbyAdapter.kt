package ch.epfl.sdp.lobby.game

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.GameLobbyPlayerCellBinding
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.lobby.PlayerParametersFragment
import kotlinx.android.synthetic.main.game_lobby_player_cell.view.*
import kotlin.coroutines.coroutineContext

class GameLobbyAdapter(
        private var players : List<Participation>, private val callerId : Int ) : RecyclerView.Adapter<GameLobbyAdapter.GameLobbyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameLobbyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.game_lobby_player_cell,parent,false)
        return GameLobbyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: GameLobbyViewHolder, position: Int) {
        val player = players[position]
        holder.display(player.user.name, factionToString(player.faction), isReadyToString(player.ready),
                callerId == player.user.uid)
    }

    private  fun  isReadyToString(isReady : Boolean) : String {
        return if (isReady) "Ready"
        else "Not Ready"
    }

    private  fun  factionToString(faction : PlayerParametersFragment.Faction) : String {
        return if (faction == PlayerParametersFragment.Faction.PREY) "PREY"
        else "PREDATOR"
    }

    class GameLobbyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun display(name : String, faction : String, isReady : String, displaySelectionFragment : Boolean)  {

            val factionLayout = itemView.faction_layout
            factionLayout.removeAllViews()

            if (displaySelectionFragment) {
                try {
                    val activity = factionLayout.context as AppCompatActivity
                    activity.supportFragmentManager.beginTransaction().add(
                            factionLayout.id, PlayerParametersFragment()
                    ).commit()
                }
                catch (e : ClassCastException ) {
                    Log.d(TAG, "Can't get the fragment manager with this")
                }
            }
            else {
                val factionTextView = TextView(factionLayout.context)
                factionTextView.text = faction
                factionLayout.addView(factionTextView)
            }

            itemView.player_name.text = name
            itemView.player_is_ready.text = isReady
        }

    }

}