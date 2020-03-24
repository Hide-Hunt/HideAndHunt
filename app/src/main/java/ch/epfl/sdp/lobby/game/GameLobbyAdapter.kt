package ch.epfl.sdp.lobby.game

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.R
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import kotlinx.android.synthetic.main.game_lobby_player_cell.view.*

/**
 * Adapter for the game lobby recyclerView
 */
class GameLobbyAdapter(
        private var players : List<Participation>,
        private var playerId : Int, private var adminId: Int) : RecyclerView.Adapter<GameLobbyAdapter.GameLobbyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameLobbyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.game_lobby_player_cell,parent,false)
        return GameLobbyViewHolder(view,playerId,adminId)
    }


    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: GameLobbyViewHolder, position: Int) {
        val player = players[position]
        holder.display(player)
    }

    /**
     * View holder for players in the game lobby
     */
    class GameLobbyViewHolder(itemView: View,
                              private var playerId : Int, private var adminId: Int) : RecyclerView.ViewHolder(itemView) {

        fun display(participation: Participation)  {

            itemView.player_faction.text = factionToString(participation.faction)
            itemView.player_name.text = participation.user.name
            itemView.player_is_ready.text = isReadyToString(participation.ready)
            if (adminId == participation.user.uid) itemView.admin_logo.setImageResource(R.drawable.star_icon)
            else itemView.admin_logo.setImageResource(0)
            if (playerId == participation.user.uid)itemView.setBackgroundColor(Color.GRAY)
            else itemView.setBackgroundColor(Color.LTGRAY)

        }

        private  fun  factionToString(faction : PlayerFaction) : String {
            return if (faction == PlayerFaction.PREY) "PREY"
            else "PREDATOR"
        }

        private  fun  isReadyToString(isReady : Boolean) : String {
            return if (isReady) "Ready"
            else "Not Ready"
        }

    }

}