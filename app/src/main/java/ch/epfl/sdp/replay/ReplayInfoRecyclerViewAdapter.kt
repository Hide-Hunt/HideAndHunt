package ch.epfl.sdp.replay

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.epfl.sdp.R


import ch.epfl.sdp.replay.ReplayInfoFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_replay_info.view.*

/**
 * [RecyclerView.Adapter] that can display a [ReplayInfo] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class ReplayInfoRecyclerViewAdapter(
        private val mValues: List<ReplayInfo>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<ReplayInfoRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ReplayInfo
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item.gameID)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_replay_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position].also {
            holder.mIdView.text = "Game #${it.gameID}"
            holder.mContentView.text = it.startTimestamp.toString()
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.gameName
        val mContentView: TextView = mView.playerFaction

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
