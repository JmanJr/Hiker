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

class PostTrailAdapter(private val viewModel: HikerViewModel,
    // If true call notifyDataSetChanged if unfavorited
                       private val unfavoriteIsRemove: Boolean = false)
    : ListAdapter<Trail, PostTrailAdapter.VH>(RedditDiff()) {

    private var trails = listOf<Trail>()


    class RedditDiff : DiffUtil.ItemCallback<Trail>() {

        override fun areItemsTheSame(oldItem: Trail, newItem: Trail): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Trail, newItem: Trail): Boolean {
            return oldItem.name== newItem.name
                    && oldItem.summary== newItem.summary
                    && oldItem.stars == newItem.stars
        }
    }



    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.name)

        var stars = itemView.findViewById<TextView>(R.id.rating)

        var comments = itemView.findViewById<TextView>(R.id.summary)
        var theFavView = itemView.findViewById<ImageView>(R.id.trailFav)

        init {
            theFavView.setOnClickListener{
                    val position = adapterPosition
                    // Toggle Favorite
                    if(viewModel.isFav(trails[position])) {
                        viewModel.removeFav(trails[position])
                    } else {
                        viewModel.addFav(trails[position])
                    }
                    notifyItemChanged(position)
            }
        }

        fun bind(item: Trail?) {
            if(item == null) return
            title.text = item.name
            println("bind")
//
            stars.text = item.stars.toString()
            comments.text = item.summary

            if (viewModel.isFav(item)) {
                theFavView.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                theFavView.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_row_post, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        println("position: $position")
        holder.bind(trails[holder.adapterPosition])
    }

    fun submitPosts(items: List<Trail>) {
        trails = items
        println("the count of favs: ${items.size}")
        notifyDataSetChanged()
    }


    override fun getItemCount() = trails.size

}

