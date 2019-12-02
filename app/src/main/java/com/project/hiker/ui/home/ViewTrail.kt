package com.project.hiker.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.project.hiker.R
import kotlinx.android.synthetic.main.view_trail.*


class ViewTrail : AppCompatActivity() {

    var lat: Float = 0.toFloat()
    var long: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_trail)
        setSupportActionBar(toolbar)

        // set up the backbutton on actionbar
        val actionbar = supportActionBar
        actionbar!!.title = intent.getStringExtra("name")
        actionbar.setDisplayHomeAsUpEnabled(true)

        title_text.text = intent.getStringExtra("name")
        summary_text.text = intent.getStringExtra("summary")

        lat = intent.getFloatExtra("lat", 10000.toFloat())
        long = intent.getFloatExtra("long", 10000.toFloat())

        if (lat == 10000F || long == 10000F) {
            mapButton.visibility = View.INVISIBLE
        } else {
            mapButton.setOnClickListener {
                openMaps()
            }
        }

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
    }

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
