package com.project.hiker.ui.home

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.project.hiker.R
import kotlinx.android.synthetic.main.view_trail.*

// view one trail
class ViewTrail : AppCompatActivity() {

    // store the lat and long for the google maps button
    var lat: Float = 0.toFloat()
    var long: Float = 0.toFloat()

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

            // some wild geocoding going on here...
            // just try to fetch. If no address, no error, just empty
            var tempAddress: MutableList<Address> = mutableListOf()
            val geocoder  = Geocoder(this)

            try {
                tempAddress = geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)
            } catch (e: Exception) {
            }

            if (tempAddress.count() > 0)
                address.text = tempAddress[0].getAddressLine(0)
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

        if (intent.getStringExtra("length") != null)
            length.text = intent.getStringExtra("length") + " Miles"
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

    // just goes back if back button pressed in actionbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
