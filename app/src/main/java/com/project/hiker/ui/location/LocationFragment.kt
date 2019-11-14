package com.project.hiker.ui.location

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.project.hiker.R
import com.project.hiker.ui.home.HomeFragment
import kotlinx.android.synthetic.main.location_fragment.*
import android.R.attr.fragment
import android.R.attr.fragment
import kotlinx.android.synthetic.main.content_main.*


class LocationFragment: Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.location_fragment, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // pictureButton from XML
        submitLocationBut.setOnClickListener {
            activity?.apply {
                Toast.makeText(
                    this, "Location Changed",
                    Toast.LENGTH_LONG
                ).show()
            }
            val manager = fragmentManager
            val transaction = manager?.beginTransaction()
            transaction?.replace(
                R.id.nav_host_fragment,
                HomeFragment.newInstance(locationET.text.toString())
            )
            transaction?.commit()
        }

    }
}
