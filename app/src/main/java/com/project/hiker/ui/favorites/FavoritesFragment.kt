package com.project.hiker.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.hiker.R
import com.project.hiker.ui.home.HikerViewModel
import com.project.hiker.ui.home.PostTrailAdapter
import kotlinx.android.synthetic.main.favorites_fragment.*

// The logic for the favorites screen.
class FavoritesFragment: Fragment() {

    // shared viewmodel, and an updated flag to let home page know if it needs to reload.
    private lateinit var viewModel: HikerViewModel
    public var updated = false

    // pass in the shared viewmodel created in MainActivity
    companion object {
        fun newInstance(viewModel: HikerViewModel): FavoritesFragment {
            val fragment = FavoritesFragment()
            fragment.viewModel = viewModel
            return fragment
        }
    }

    private fun initRecyclerView(root: View): PostTrailAdapter {
        val rv = root.findViewById<RecyclerView>(R.id.favResults)
        val adapter = PostTrailAdapter(viewModel, true)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        return adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.favorites_fragment, container, false)
        val adapter = initRecyclerView(root)

        // Favorites observer. If there are no favorites, show the no favs message,
        // otherwise show recyclerview. If a change is made, tell home screen to update.
        viewModel.getFavs().observe(this, Observer {
            if (it.count() == 0) {
                no_favs.visibility = View.VISIBLE
            } else {
                no_favs.visibility = View.GONE
            }
            adapter.submitPosts(it)
            updated = true
        })
        return root
    }
}