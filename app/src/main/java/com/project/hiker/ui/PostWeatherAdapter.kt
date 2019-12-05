package com.project.hiker.ui

import android.util.Log
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
class PostWeatherAdapter()
    : ListAdapter<Trail, PostWeatherAdapter.VH>(WeatherDiff()) {

    private var weather = listOf<Trail>()

    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var highTemp = itemView.findViewById<TextView>(R.id.hiTempTV)
        var lowTemp = itemView.findViewById<TextView>(R.id.lowTempTV)
        var wind = itemView.findViewById<TextView>(R.id.windTV)
        var forecast = itemView.findViewById<TextView>(R.id.forecastTV)
        var rain = itemView.findViewById<TextView>(R.id.rainTV)
        var snow = itemView.findViewById<ImageView>(R.id.snowTV)

    /*
        fun bind(item: Weather?) {
            if(item == null) return

            // fill in row data
            highTemp.text = item.highTemp
            lowTemp.text = item.stars.toString() + " / 5.0"
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
        */

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_row_post, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        //holder.bind(trails[holder.adapterPosition])
    }

    fun submitPosts(items: List<Trail>) {
        weather = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = weather.size

    class WeatherDiff : DiffUtil.ItemCallback<Trail>() {

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

