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
import com.project.hiker.R

class HomeFragment : Fragment() {

    private lateinit var viewModel: HikerViewModel

    companion object {
        //  Why fragments need a constructor with zero parameters
        //    https://stackoverflow.com/questions/10450348/do-fragments-really-need-an-empty-constructor
        // If create is false, log in screen and log in action, otherwise create account screen and action
        internal fun newInstance(address: String): HomeFragment {
            val homeFragment = HomeFragment()
            val b = Bundle()
            b.putString("address", address)
            homeFragment.arguments = b
            println(homeFragment.arguments)
            return homeFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        if(arguments != null) {
            println("bundled address: ${arguments?.get("address")}")
            textView.text = arguments?.get("address").toString()
        }



        viewModel =
            ViewModelProviders.of(this)[HikerViewModel::class.java]

        viewModel.fetchTrails()
        viewModel.getTrails().observe(this, Observer {
            Log.d("TRAILS: ", it.toString())
        })
        
        return root
    }
}