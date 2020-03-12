package ch.epfl.sdp.lobby.global

import android.content.res.Resources.getSystem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.R
import ch.epfl.sdp.game.data.Game

class GlobalLobbyAdapter(private val data: List<Game>) :  RecyclerView.Adapter<GlobalLobbyAdapter.MyViewHolder>() {

    class MyViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalLobbyAdapter.MyViewHolder {
        val linearLayout = LayoutInflater.from(parent.context).inflate(R.layout.global_lobby_item_view, parent, false) as LinearLayout
        return MyViewHolder(linearLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        (holder.linearLayout.getChildAt(0) as TextView).text = data[position].name
        (holder.linearLayout.getChildAt(1) as TextView).text = "Created by: "+data[position].admin
        (holder.linearLayout.getChildAt(2) as TextView).text = "Players: "+data[position].name
    }

    override fun getItemCount() = data.size

}
