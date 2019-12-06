package com.project.hiker.ui.home

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.hiker.api.*
import kotlinx.coroutines.launch

// viewmodel. lotta shared data here, we do most of the work
// on startup to make experience faster during runtime
class HikerViewModel: ViewModel() {
    // APIs and repository
    private val trailsApi = TrailsApi.create()
    private val latLonApi = LatLonApi.create()
    private val trailRepository = HikerRepository(trailsApi, latLonApi)
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // major data lists
    private var trails = MutableLiveData<List<Trail>>().apply {
        value = mutableListOf()
    }
    private var favTrails = MutableLiveData<MutableList<Trail>>().apply {
        value = mutableListOf()
    }

    // address related vars
    private var currentAddress = MutableLiveData<String>()
    private var currentCity = MutableLiveData<String>()
    private var currentStateIndex = MutableLiveData<Int>()

    // optional filters
    private var maxDistance = MutableLiveData<String>()
    private var currentSortIndex = MutableLiveData<Int>()
    private var minLength = MutableLiveData<String>()
    private var minStars = MutableLiveData<Int>()


    fun fetchTrails() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // convert sort index into the wording the API wants. default = quality
        var sort = if (currentSortIndex.value == null || (currentSortIndex.value)!!.toInt() == 0) "quality"
            else "distance"
        trails.postValue(trailRepository.getTrails(currentAddress.value.toString(), maxDistance.value.toString(),
            sort, minLength.value.toString(), minStars.value.toString()))
    }

    fun fetchFavTrails(ids: String) = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        favTrails.postValue(trailRepository.getTrailsByIds(ids))
    }

    // just get trails. never modify outside of fetch
    fun getTrails() : LiveData<List<Trail>> {
        return trails
    }

    // everything fav related.
    internal fun getFavs(): MutableLiveData<MutableList<Trail>> {
        return favTrails
    }

    fun addFav(trail: Trail) {
        favTrails.value!!.add(trail)
        favTrails.postValue(favTrails.value!!)
        println(favTrails.value.toString())

        // check if anyone is signed in this very second after some horrible nightmare situations that
        // we will never risk again
        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("favTrails")
                .child(trail.id).setValue(trail.id)
        }
    }

    fun isFav(trailPost: Trail): Boolean {
        return favTrails.value?.contains(trailPost) ?: false
    }

    fun setFavs(favs: MutableList<Trail>) {
        favTrails.postValue(favs)
    }


    fun removeFav(trail: Trail) {
        favTrails.value!!.remove(trail)
        favTrails.postValue(favTrails.value!!)

        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("favTrails")
                .child(trail.id).removeValue()
        }
    }

    // address related functions
    fun getAddress(): LiveData<String> {
        return currentAddress
    }

    fun setAddress(newAddress: String) {
        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("currentFilters")
                .child("address").setValue(newAddress)
            currentAddress.postValue(newAddress)
        }
    }

    // nothing special for any of the fields from here on out. self explanatory
    fun setCity(newCity: String) {
        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("currentFilters")
                .child("city").setValue(newCity)
            currentCity.postValue(newCity)
        }
    }

    fun getCity(): LiveData<String> {
        return currentCity
    }

    fun setStateIndex(newIndex: Int) {
        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("currentFilters")
                .child("stateIndex").setValue(newIndex)
            currentStateIndex.postValue(newIndex)
        }
    }

    fun getStateIndex(): LiveData<Int> {
        return currentStateIndex
    }

    fun setMaxDistance(newMaxDistance: String) {
        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("currentFilters")
                .child("maxDistance").setValue(newMaxDistance)
            maxDistance.postValue(newMaxDistance)
        }
    }

    fun getMaxDistance(): LiveData<String> {
        return maxDistance
    }

    fun setSortIndex(newIndex: Int) {
        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("currentFilters")
                .child("sortIndex").setValue(newIndex)
            currentSortIndex.postValue(newIndex)
        }
    }

    fun getSortIndex(): LiveData<Int> {
        return currentSortIndex
    }

    fun setMinLength(newMinLength: String) {
        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("currentFilters")
                .child("minLength").setValue(newMinLength)
            minLength.postValue(newMinLength)
        }
    }

    fun getMinLength(): LiveData<String> {
        return minLength
    }

    fun setMinStars(newMinStars: Int) {
        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("currentFilters")
                .child("minStars").setValue(newMinStars)
            minStars.postValue(newMinStars)
        }
    }

    fun getMinStars(): LiveData<Int> {
        return minStars
    }

    // calls the companion class's viewTrail method
    fun viewTrail(context: Context, trail: Trail) {
        Companion.viewTrail(context, trail)
    }

    // Convenient place to put it as it is shared. Borrowed from reddit. Thanks Witchel!
    companion object {
        // create one post activity
        fun viewTrail(context: Context, trail: Trail) {
            // put all the relevant data into the intent and start activity
            val intent = Intent(context, ViewTrail::class.java)
            intent.putExtra("name", trail.name)
            intent.putExtra("summary", trail.summary)
            intent.putExtra("lat", trail.latitude)
            intent.putExtra("long", trail.longitude)
            intent.putExtra("conditionDetails", trail.conditionDetails)
            intent.putExtra("difficulty", trail.difficulty)
            intent.putExtra("conditionStatus", trail.conditionStatus)
            intent.putExtra("length", trail.length)
            ContextCompat.startActivity(context, intent, null)
        }
    }
}