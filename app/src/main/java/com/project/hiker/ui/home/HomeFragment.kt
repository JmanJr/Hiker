package com.project.hiker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.hiker.R
import com.project.hiker.api.Trail
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HikerViewModel
    private lateinit var postTrailAdapter: PostTrailAdapter

    companion object {
        internal fun newInstance(address: String): HomeFragment {
            val homeFragment = HomeFragment()
            val b = Bundle()
            b.putString("address", address)
            homeFragment.arguments = b
            println(homeFragment.arguments)
            return homeFragment
        }
    }

    private fun submitPosts(trails: List<Trail>, adapter: PostTrailAdapter) {
        adapter.submitPosts(trails)

        swipeRefreshLayout.setOnRefreshListener {
            //            viewModel.addPost()
            swipeRefreshLayout.isRefreshing = false
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

        viewModel =
            ViewModelProviders.of(this)[HikerViewModel::class.java]

        if(arguments != null) {
            println("bundled address: ${arguments?.get("address")}")
            val newAddress = arguments?.get("address").toString()
            if (newAddress != null && newAddress.isNotBlank())
                viewModel.setAddress(newAddress)
        } else {
            viewModel.setAddress("Austin, Texas")
        }

        initAdapter(root)
        println("view model in Home: " + viewModel)

        viewModel.getAddress().observe(this, Observer {
            Log.d("ADDRESS", it)
            textView.text = it
            viewModel.fetchTrails()
        })

        viewModel.getTrails().observe(this, Observer {
            println("number of trails: ${it.size}")
            Log.d("TRAILS: ", it.toString())
            submitPosts(it, this.postTrailAdapter)
        })

        return root
    }
}