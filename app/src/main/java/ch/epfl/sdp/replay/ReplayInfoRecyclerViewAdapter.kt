package ch.epfl.sdp.replay

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.epfl.sdp.R
import ch.epfl.sdp.replay.ReplayInfoListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_replay_info.view.*
import java.text.DateFormat

/**
 * [RecyclerView.Adapter] that can display a [ReplayInfo] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class ReplayInfoRecyclerViewAdapter(
        var mValues: List<ReplayInfo>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<ReplayInfoRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ReplayInfo
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_replay_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position].also {
            holder.mIdView.text = it.gameID.toString()
            holder.mContentView.text = it.startTimestamp.toString()

            holder.mView.gameName.text = "Game #${it.gameID}"
            holder.mView.playerFaction.text = it.winningFaction.toString()
            holder.mView.score.text = "WIN/LOOSE"
            with(it.endTimestamp - it.startTimestamp) {
                holder.mView.game_duration.text = String.format("%d:%02d:%02d",
                        this / 3600,
                        (this % 3600) / 60,
                        (this % 60)
                )
            }

            holder.mView.date.text = DateFormat.getDateInstance().format(it.startTimestamp*1000)
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
