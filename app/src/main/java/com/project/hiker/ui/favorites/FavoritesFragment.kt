package com.project.hiker.ui.favorites

//import android.R
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


class FavoritesFragment: Fragment() {

    private lateinit var viewModel: HikerViewModel
    public var updated = false

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

        // Process menu for this fragment
        val root = inflater.inflate(R.layout.favorites_fragment, container, false)
        val adapter = initRecyclerView(root)
        viewModel.getFavs().observe(this, Observer {
            adapter.submitPosts(it)
            updated = true
        })
        return root
    }
}