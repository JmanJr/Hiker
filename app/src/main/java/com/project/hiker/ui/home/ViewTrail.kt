package com.project.hiker.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.project.hiker.R
import com.project.hiker.api.WeatherApi
import com.project.hiker.api.WeatherObj
import kotlinx.android.synthetic.main.view_trail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// view one trail
class ViewTrail : AppCompatActivity() {

    // store the lat and long for the google maps button
    var lat: Float = 0.toFloat()
    var long: Float = 0.toFloat()
    val viewModel = HikerViewModel()

    lateinit var weatherList: List<WeatherObj>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_trail)

        // set up the backbutton on actionbar
        val actionbar = supportActionBar
        actionbar!!.title = intent.getStringExtra("name")
        actionbar.setDisplayHomeAsUpEnabled(true)

        // add in the information
        title_text.text = intent.getStringExtra("name")
        summary_text.text = intent.getStringExtra("summary")

        // get lat and long if possible. If not possible, hide the button
        lat = intent.getFloatExtra("lat", 10000.toFloat())
        long = intent.getFloatExtra("long", 10000.toFloat())
        if (lat == 10000F || long == 10000F) {
            mapButton.visibility = View.INVISIBLE
        } else {
            mapButton.setOnClickListener {
                openMaps()
            }
        }

        // checks for conditions and adds them if possible.
        var conditionString: String = ""
        if (intent.getStringExtra("conditionStatus") != null)
            conditionString += intent.getStringExtra("conditionStatus")
        if (intent.getStringExtra("conditionDetails") != null) {
            if (conditionString.isNotBlank()) {
                conditionString += ": "
            }
            conditionString += intent.getStringExtra("conditionDetails")
        }
        if (conditionString.isNotBlank())
            condition.text = conditionString

        if (intent.getStringExtra("difficulty") != null)
            difficulty.text = intent.getStringExtra("difficulty")

        viewModel.fetchWeathers(lat, long)
        //weatherList = viewModel.getWeathers()
    }

    // open google maps. https://developers.google.com/maps/documentation/urls/android-intents
    fun openMaps() {
        val gmmIntentUri: Uri = Uri.parse("geo:" + lat.toString() + "," + long.toString())
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }



}
