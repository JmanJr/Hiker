package com.project.hiker.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.hiker.R

import kotlinx.android.synthetic.main.view_trail.*

class ViewTrail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_trail)
        setSupportActionBar(toolbar)

        // set up the backbutton on actionbar
        val actionbar = supportActionBar
        actionbar!!.title = intent.getStringExtra("name")
        actionbar.setDisplayHomeAsUpEnabled(true)

        title_text.text = intent.getStringExtra("name")
    }

    // just goes back if back button pressed in actionbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
