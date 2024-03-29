package com.project.hiker.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.hiker.R
import com.project.hiker.api.Trail

/**
 * Created by witchel on 8/25/2019
 */

// updated from reddit's adapter, thank you witchel.
class PostTrailAdapter(private val viewModel: HikerViewModel,
    // If true call notifyDataSetChanged if unfavorited
                       private val unfavoriteIsRemove: Boolean = false)
    : ListAdapter<Trail, PostTrailAdapter.VH>(TrailsDiff()) {

    private var trails = listOf<Trail>()

    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.name)
        var stars = itemView.findViewById<TextView>(R.id.rating)
        var summary = itemView.findViewById<TextView>(R.id.summary)
        var length = itemView.findViewById<TextView>(R.id.lengthTV);
        var difficulty = itemView.findViewById<TextView>(R.id.difficultyTV)
        var theFavView = itemView.findViewById<ImageView>(R.id.trailFav)


        fun bind(item: Trail?) {
            if(item == null) return

            // view the trail's specifics on click
            itemView.setOnClickListener {
                viewModel.viewTrail(itemView.context, item)
            }

            // fill in row data
            title.text = item.name
            stars.text = item.stars.toString() + " / 5.0"
            summary.text = item.summary
            length.text = item.length.toString() + " miles"
            difficulty.text = item.difficulty
            if (viewModel.isFav(item)) {
                theFavView.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                theFavView.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }

            // update favorites on favview click
            theFavView.setOnClickListener {
                // unfavorite. notify if on favorites screen
                if (viewModel.isFav(item)) {
                    theFavView.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    viewModel.removeFav(item)
                    if (unfavoriteIsRemove) {
                        notifyDataSetChanged()
                    }
                }
                // favorite
                else {
                    theFavView.setImageResource(R.drawable.ic_favorite_black_24dp)
                    viewModel.addFav(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_row_trail, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(trails[holder.adapterPosition])
    }

    fun submitPosts(items: List<Trail>) {
        trails = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = trails.size

    class TrailsDiff : DiffUtil.ItemCallback<Trail>() {

        override fun areItemsTheSame(oldItem: Trail, newItem: Trail): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Trail, newItem: Trail): Boolean {
            return oldItem.name== newItem.name
                    && oldItem.summary== newItem.summary
                    && oldItem.stars == newItem.stars
        }
    }

}

