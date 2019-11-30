package com.project.hiker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.project.hiker.R
import com.project.hiker.api.Trail
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HikerViewModel
    lateinit var postTrailAdapter: PostTrailAdapter
    private lateinit var swipe: SwipeRefreshLayout

    companion object {
        internal fun newInstance(viewModel: HikerViewModel): HomeFragment {
            val fragment = HomeFragment()
            fragment.viewModel = viewModel
            return fragment
        }
    }

    fun submitPosts(trails: List<Trail>, adapter: PostTrailAdapter) {
        adapter.submitPosts(trails)

        swipeRefreshLayout.setOnRefreshListener {
            //            viewModel.addPost()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initSwipeLayout(root: View) {
        swipe = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipe.setOnRefreshListener {
            viewModel.fetchTrails()
            swipe.isRefreshing = false
        }
    }

    private fun initAdapter(root: View) {
        val rv = root.findViewById<RecyclerView>(R.id.searchResults)
        postTrailAdapter = PostTrailAdapter(this.viewModel)
        rv.adapter = postTrailAdapter
        rv.layoutManager = LinearLayoutManager(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        initAdapter(root)
        initSwipeLayout(root)

        viewModel.getAddress().observe(this, Observer {
            swipe.isRefreshing = true
            submitPosts(mutableListOf(), this.postTrailAdapter)
            textView.text = it
            viewModel.fetchTrails()
            swipe.isRefreshing = false
        })

        viewModel.getTrails().observe(this, Observer {
            swipe.isRefreshing = true
            if (it.count() == 0) {
                no_trails.visibility = View.VISIBLE
                searchResults.visibility = View.INVISIBLE
            }
            else {
                no_trails.visibility = View.INVISIBLE
                searchResults.visibility = View.VISIBLE
            }
            submitPosts(it, this.postTrailAdapter)
            swipe.isRefreshing = false
        })

        return root
    }
}