package com.project.hiker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*



import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.hiker.api.Trail
import com.project.hiker.ui.favorites.FavoritesFragment
import com.project.hiker.ui.home.HikerViewModel
import com.project.hiker.ui.home.HomeFragment
import com.project.hiker.ui.location.LocationFragment


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123
    private lateinit var viewModel: HikerViewModel
    private lateinit var home: HomeFragment
    private lateinit var favs: FavoritesFragment
    private lateinit var filter: LocationFragment
    private lateinit var currentFrag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            createSignInIntent()
        }

        setSupportActionBar(toolbar)

        // getes our data in the viewmodel
        viewModel = ViewModelProviders.of(this)[HikerViewModel::class.java]
        setupDatabase()

        // creates these once.
        home = HomeFragment.newInstance(viewModel)
        favs = FavoritesFragment.newInstance(viewModel)
        filter = LocationFragment.newInstance(viewModel)

        // sets up a bottomnavigationview
        bottomNavigationView.setOnNavigationItemSelectedListener(customOnNavigationItemSelectedListener)
        bottomNavigationView.menu.findItem(R.id.navigation_home).isChecked = true
        (this as AppCompatActivity).supportActionBar?.title = "Trails"

        // gets us started
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.homeContainer, home)
                .commit()
            currentFrag = "home"
        }
    }

    // sets up database and grabs all our goodies. One and done,
    // rest is all viewmodel for users
    private fun setupDatabase() {
        val addressListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentFirebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                if (currentFirebaseUser != null) {
                    val address =
                        dataSnapshot.child("users").child(currentFirebaseUser.uid).child("currentFilters").child("address")
                            .getValue(String::class.java)
                    if (address == null) {
                        viewModel.setAddress("Austin, Texas")
                    } else {
                        viewModel.setAddress(address)
                    }

                    val newMaxDistance =
                        dataSnapshot.child("users").child(currentFirebaseUser.uid).child("currentFilters").child("maxDistance")
                            .getValue(String::class.java)
                    if (newMaxDistance != null) {
                        viewModel.setMaxDistance(newMaxDistance)
                    }

                    val sortIndex =
                        dataSnapshot.child("users").child(currentFirebaseUser.uid).child("currentFilters").child("sortIndex")
                            .getValue(Int::class.java)
                    if (sortIndex != null) {
                        viewModel.setSortIndex(sortIndex)
                    }

                    val city =
                        dataSnapshot.child("users").child(currentFirebaseUser.uid).child("currentFilters").child("city")
                            .getValue(String::class.java)
                    if (city != null) {
                        viewModel.setCity(city)
                    }

                    val stateIndex =
                        dataSnapshot.child("users").child(currentFirebaseUser.uid).child("currentFilters").child("stateIndex")
                            .getValue(Int::class.java)
                    if (stateIndex != null) {
                        viewModel.setStateIndex(stateIndex)
                    }

                    val minLength =
                        dataSnapshot.child("users").child(currentFirebaseUser.uid).child("currentFilters").child("minLength")
                            .getValue(String::class.java)
                    if (minLength != null) {
                        viewModel.setMinLength(minLength)
                    }

                    val minStars =
                        dataSnapshot.child("users").child(currentFirebaseUser.uid).child("currentFilters").child("minStars")
                            .getValue(Int::class.java)
                    if (minStars != null) {
                        viewModel.setMinStars(minStars)
                    }

                    val favTrails =
                        dataSnapshot.child("users").child(currentFirebaseUser.uid).child("favTrails")
                            .children

                    var favTrailIds = ""
                    favTrails.forEach {
                        favTrailIds += it.getValue(String::class.java)!! + ","
                    }
                    if (favTrailIds.isNotEmpty()) {
                        favTrailIds.trimEnd()
                        viewModel.fetchFavTrails(favTrailIds)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                viewModel.setAddress("Austin, Texas")
            }
        }

        val database = FirebaseDatabase.getInstance().reference
        database.addListenerForSingleValueEvent(addressListener)
    }

    // our customnavlistener. shows and hides fragments instead of swapping them. Keeps things smooth
    private val customOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        if (menuItem.isChecked)
            return@OnNavigationItemSelectedListener false

        // switch for which fragment to show. the one chosen is all in the name
        menuItem.isChecked = true
        when (menuItem.itemId) {
            R.id.navigation_home -> {
                (this as AppCompatActivity).supportActionBar?.title = "Trails"
                // big ol' transaction. If favs were changed, then update to reflect changes.
                // then, if we already have added this, show it, otherwise add it. Then
                // hide everything else.
                supportFragmentManager.beginTransaction().apply {
                    if(currentFrag.equals("favs") && favs.updated) {
                        home.submitTrails(viewModel.getTrails().value!!, home.postTrailAdapter)
                        favs.updated = false
                    }

                    if (home.isAdded) {
                        show(home)
                    } else {
                        add(R.id.homeContainer, home)
                    }

                    supportFragmentManager.fragments.forEach {
                        if (it != home && it.isAdded) {
                            hide(it)
                        }
                    }
                }.commit()
                currentFrag = "home"

                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_filter -> {
                (this as AppCompatActivity).supportActionBar?.title = "Filter"

                supportFragmentManager.beginTransaction().apply {
                    if (filter.isAdded) {
                        show(filter)
                    } else {
                        add(R.id.filterContainer, filter)
                    }

                    supportFragmentManager.fragments.forEach {
                        if (it != filter && it.isAdded) {
                            hide(it)
                        }
                    }
                }.commit()
                currentFrag = "filter"

                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_favorites -> {
                (this as AppCompatActivity).supportActionBar?.title = "Favorites"

                supportFragmentManager.beginTransaction().apply {
                    if (favs.isAdded) {
                        show(favs)
                    } else {
                        add(R.id.favsContainer, favs)
                    }

                    supportFragmentManager.fragments.forEach {
                        if (it != favs && it.isAdded) {
                            hide(it)
                        }
                    }
                }.commit()
                currentFrag = "favs"

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    // SOURCE: Code from starter code in peck assignment
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // SOURCE: Modified from source code in peck assignment
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // firebase functions for sign in and out
    fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setTheme(R.style.LoginTheme)
                .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_create_intent]
    }


    fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
                createSignInIntent()
            }
        // [END auth_fui_signout]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                // success
                setupDatabase()

            } else {
                createSignInIntent()
            }
        } else {
            createSignInIntent()
        }
    }
}
