package ch.epfl.sdp.lobby.global

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Game
import ch.epfl.sdp.lobby.LobbyActivity

class GlobalLobbyAdapter(val data: List<Game>) :  RecyclerView.Adapter<GlobalLobbyAdapter.MyViewHolder>() {

    class MyViewHolder(val linearLayout: LinearLayout, val data: List<Game>) : RecyclerView.ViewHolder(linearLayout), View.OnClickListener {

        override fun onClick(v: View?) {
            val game = data[layoutPosition]
            Toast.makeText(v?.context, game.name, Toast.LENGTH_SHORT).show()
            val intent = Intent(v?.context, LobbyActivity::class.java) //TODO: Call with the game as param
            v?.context?.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalLobbyAdapter.MyViewHolder {
        val linearLayout = LayoutInflater.from(parent.context).inflate(R.layout.global_lobby_item_view, parent, false) as LinearLayout
        return MyViewHolder(linearLayout, data)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener(holder)
        (holder.linearLayout.getChildAt(0) as TextView).text = data[position].name
        (holder.linearLayout.getChildAt(1) as TextView).text = holder.itemView.context.getString(R.string.game_created_by)+data[position].admin
        (holder.linearLayout.getChildAt(2) as TextView).text = holder.itemView.context.getString(R.string.player_in_lobby)+data[position].participation.size
    }

    override fun getItemCount() = data.size

}
