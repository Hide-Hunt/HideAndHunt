package ch.epfl.sdp.lobby.game

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Faction
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.user.IUserRepo
import kotlinx.android.synthetic.main.game_lobby_player_cell.view.*

/**
 * Adapter for the game lobby recyclerView
 * @param participation List<Participation>: list of player participations
 * @param playerId Int: current player's id
 * @param adminId  Int: game admin'is id
 */
class GameLobbyAdapter(
        private val participation: List<Participation>,
        private val playerId: String,
        private val adminId: String,
        private val userRepo: IUserRepo
) : RecyclerView.Adapter<GameLobbyAdapter.GameLobbyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameLobbyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.game_lobby_player_cell, parent, false)
        return GameLobbyViewHolder(view, playerId, adminId, userRepo)
    }

    override fun getItemCount(): Int {
        return participation.size
    }

    override fun onBindViewHolder(holder: GameLobbyViewHolder, position: Int) {
        val player = participation[position]
        holder.display(player)
    }

    /**
     * View holder for players in the game lobby
     * @param itemView View: viewHolder's view for the cell
     * @param playerId Int: current player's id
     * @param adminId  Int: game admin'is id
     */
    class GameLobbyViewHolder(itemView: View,
                              private val playerId: String,
                              private val adminId: String,
                              private val userRepo: IUserRepo
    ) : RecyclerView.ViewHolder(itemView) {

        /**
         * Displays a given [Participation] in the [GameLobbyViewHolder]
         * @param participation Participation: the [Participation] to display
         */
        fun display(participation: Participation) {
            userRepo.getUsername(participation.userID) {username ->
                //set text views
                itemView.player_faction.text = factionToString(participation.faction)
                itemView.player_name.text = username
                itemView.player_is_ready.text = isReadyToString(participation.ready)
                //set admin logo
                if (adminId == participation.userID) itemView.admin_logo.setImageResource(R.drawable.star_icon)
                else itemView.admin_logo.setImageResource(0)
                //set cell background
                if (playerId == participation.userID) itemView.setBackgroundColor(Color.GRAY)
                else itemView.setBackgroundColor(Color.LTGRAY)
            }
        }

        private fun factionToString(faction: Faction): String {
            return if (faction == Faction.PREY) "PREY"
            else "PREDATOR"
        }

        private fun isReadyToString(isReady: Boolean): String {
            return if (isReady) "Ready"
            else "Not Ready"
        }
    }
}