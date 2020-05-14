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
import kotlin.math.max

/**
 * Adapter for the game lobby recyclerView
 * @param participations List<Participation>: list of player participations
 * @param playerId Int: current player's id
 * @param adminId  Int: game admin'is id
 */
class GameLobbyAdapter(
        private var participations: List<Participation>,
        private var playerId: Int, private var adminId: Int) : RecyclerView.Adapter<GameLobbyAdapter.GameLobbyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameLobbyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.game_lobby_player_cell, parent, false)
        return GameLobbyViewHolder(view, playerId, adminId)
    }

    override fun getItemCount(): Int {
        return participations.size
    }

    override fun onBindViewHolder(holder: GameLobbyViewHolder, position: Int) {
        val player = participations[position]
        holder.display(player)
    }

    /**
     * View holder for players in the game lobby
     * @param itemView View: viewHolder's view for the cell
     * @param playerId Int: current player's id
     * @param adminId  Int: game admin'is id
     */
    class GameLobbyViewHolder(itemView: View,
                              private var playerId: Int, private var adminId: Int) : RecyclerView.ViewHolder(itemView) {

        /**
         * Displays a given [Participation] in the [GameLobbyViewHolder]
         * @param participation Participation: the [Participation] to display
         */
        fun display(participation: Participation) {
            //set text views
            itemView.player_faction.text = factionToString(participation.faction)
            itemView.player_name.text = participation.username.subSequence(0, max(5, participation.username.indexOf(".")))
            itemView.player_is_ready.text = isReadyToString(participation.ready)
            //set admin logo
            if (adminId == participation.playerID) itemView.admin_logo.setImageResource(R.drawable.star_icon)
            else itemView.admin_logo.setImageResource(0)
            //set cell background
            if (playerId == participation.playerID) itemView.setBackgroundColor(Color.GRAY)
            else itemView.setBackgroundColor(Color.LTGRAY)
        }

        private fun factionToString(faction: PlayerFaction): String {
            return if (faction == PlayerFaction.PREY) "PREY"
            else "PREDATOR"
        }

        private fun isReadyToString(isReady: Boolean): String {
            return if (isReady) "Ready"
            else "Not Ready"
        }
    }
}