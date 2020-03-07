package ch.epfl.sdp.game

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ch.epfl.sdp.R

import ch.epfl.sdp.game.data.Prey

import kotlinx.android.synthetic.main.fragment_prey.view.*

/**
 * [RecyclerView.Adapter] that can display a [Prey]
 */
class PreyRecyclerViewAdapter(var mValues: ArrayList<Prey>)
    : RecyclerView.Adapter<PreyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_prey, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = "Prey %d".format(item.id)
        holder.mIdView.setImageResource( if(item.state == Prey.PreyState.ALIVE) R.drawable.ic_running_icon else R.drawable.ic_skull_icon)

        with(holder.mView) {
            tag = item
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.prey_name
        val mIdView: ImageView = mView.state

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
