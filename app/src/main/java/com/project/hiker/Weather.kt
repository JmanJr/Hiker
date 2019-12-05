package com.project.hiker.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.project.hiker.R
import com.project.hiker.api.HikerRepository
import com.project.hiker.api.LatLonApi
import com.project.hiker.api.TrailsApi
import kotlinx.android.synthetic.main.weather_fragment.*

class Weather : AppCompatActivity() {

    private val trailsApi = TrailsApi.create()
    private val latLonApi = LatLonApi.create()
    private val trailRepository = HikerRepository(trailsApi, latLonApi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_fragment)
//        val toolbar: Toolbar = findViewById(R.id.toolbar2)
//        setSupportActionBar(toolbar)
//        supportActionBar?.let{
//            initActionBar(it)
//        }
        trailNameTV.text = intent.getStringExtra("name").toString() + "Weather"
//
//        var t = intent.getStringExtra("title")
//        opTitle.text = t.toString()
//        if(t.length > 30) {
//            t = t.substring(0,30) + "..."
//        }
//        opTop.text = t.toString()
//        val imageUrl = intent.getStringExtra("imageURL")
//        val thumb = intent.getStringExtra("thumb")
//        Glide.glideFetch(imageUrl, thumb, opImage)
//
//        back.setOnClickListener {
//            Toast.makeText(it.context, "I'm trying to back", Toast.LENGTH_SHORT).show()
//            finish()
//        }

    }


//    private fun initActionBar(actionBar: ActionBar) {
//        // Disable the default and enable the custom
//        actionBar.setDisplayShowTitleEnabled(false)
//        actionBar.setDisplayShowCustomEnabled(true)
//        val customView: View =
//            layoutInflater.inflate(R.layout.action_one_post, null)
//        // Apply the custom view
//        actionBar.customView = customView
//    }
}
