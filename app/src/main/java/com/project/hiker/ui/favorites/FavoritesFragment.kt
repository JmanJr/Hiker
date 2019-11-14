package com.project.hiker.ui.favorites

//import android.R
import android.os.Bundle
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
    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
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
        (activity as AppCompatActivity).supportActionBar?.title = "Favorites"
        viewModel = activity?.run {
            ViewModelProviders.of(this)[HikerViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        val vmp = ViewModelProviders.of(this)[HikerViewModel::class.java]
        println("view model in favorites: " + vmp)
//        Log.d(javaClass.simpleNa
//        Log.d(javaClass.simpleName, "onCreateView ${viewModel.selected}")
        // Process menu for this fragment
        val root = inflater.inflate(R.layout.favorites_fragment, container, false)
        val adapter = initRecyclerView(root)
        viewModel.getFavs().observe(this, Observer {
            println("in favorites fragment: " + it.size)
            adapter.submitPosts(it)
        })
        return root
    }
}