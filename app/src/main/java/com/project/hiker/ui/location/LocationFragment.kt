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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import com.google.gson.internal.bind.ArrayTypeAdapter
import com.project.hiker.ui.favorites.FavoritesFragment
import com.project.hiker.ui.home.HikerViewModel
import kotlinx.android.synthetic.main.content_main.*


class LocationFragment: Fragment() {
    private lateinit var viewModel: HikerViewModel
    companion object {
        fun newInstance(viewModel: HikerViewModel): LocationFragment {
            val fragment = LocationFragment()
            fragment.viewModel = viewModel
            return fragment
        }
    }

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
        val adapter = ArrayAdapter(
            activity!!, // Context
            android.R.layout.simple_spinner_item, // Layout
            resources.getStringArray(R.array.states_array) // Array
        )

        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        states_spinner.adapter = adapter;

        // Set an on item selected listener for spinner object
        states_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }

        viewModel.getStateIndex().observe(this, Observer {
            states_spinner.setSelection(it)
        })
        viewModel.getCity().observe(this, Observer {
            cityET.setText(it)
        })
        viewModel.getMaxDistance().observe(this, Observer {
            maxDistanceET.setText(it)
        })

        submitLocationBut.setOnClickListener {
            var address = cityET.text.toString()
            if (states_spinner.selectedItemPosition != 0) {
                if (address.isNotBlank()) {
                    address += ", "
                }
                address += states_spinner.selectedItem.toString()
            }
            val maxDistance = maxDistanceET.text.toString().toIntOrNull()

            if (address.isNullOrBlank()) {
                activity?.apply {
                    Toast.makeText(
                        this, "Invalid Address",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else if(maxDistance == null || maxDistance <= 0) {
                activity?.apply {
                    Toast.makeText(
                        this, "Invalid Max Distance",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else {
                viewModel.setAddress(address)
                viewModel.setCity(cityET.text.toString())
                viewModel.setStateIndex(states_spinner.selectedItemPosition)
                viewModel.setMaxDistance(maxDistanceET.text.toString())
                viewModel.setSort(orderByPicker.text.toString())

                activity?.apply {
                    Toast.makeText(
                        this, "Location Changed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}
