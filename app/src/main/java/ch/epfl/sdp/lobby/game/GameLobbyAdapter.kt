package ch.epfl.sdp.lobby.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Participation
import ch.epfl.sdp.lobby.PlayerParametersFragment
import kotlinx.android.synthetic.main.game_lobby_player_cell.view.*

class GameLobbyAdapter(
        private var players : List<Participation>) : RecyclerView.Adapter<GameLobbyAdapter.GameLobbyViewHolder>() {


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
        holder.display(player.user.name, factionToString(player.faction), isReadyToString(player.ready))
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

        fun display(name : String, faction : String, isReady : String)  {

                /*try {
                    val activity = factionLayout.context as AppCompatActivity
                    val tag = "CHOSE_FACTION"

                    val fragment: Fragment? = activity.supportFragmentManager.findFragmentByTag(tag)
                    if (fragment != null) activity.supportFragmentManager.beginTransaction().remove(fragment).commit()

                    activity.supportFragmentManager.beginTransaction().add(
                            factionLayout.id, PlayerParametersFragment(),tag
                    ).commit()
                }
                catch (e : ClassCastException ) {
                    Log.d(TAG, "Can't get the fragment manager with this")
                }
            }
            else {*/

            itemView.player_faction.text = faction
            itemView.player_name.text = name
            itemView.player_is_ready.text = isReady
        }

    }

}