package com.project.hiker.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.hiker.api.*
import kotlinx.coroutines.launch

class HikerViewModel: ViewModel() {

    private val trailsApi = TrailsApi.create()
    private val latLonApi = LatLonApi.create()
    private val trailRepository = HikerRepository(trailsApi, latLonApi)
    private var trails = MutableLiveData<List<Trail>>().apply {
        value = mutableListOf()
    }
    private var currentAddress = MutableLiveData<String>()
    private var currentCity = MutableLiveData<String>()
    private var currentStateIndex = MutableLiveData<Int>()
    private var favTrails = MutableLiveData<MutableList<Trail>>().apply {
        value = mutableListOf()
    }
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var maxDistance = MutableLiveData<String>()
    private var currentSortIndex = MutableLiveData<Int>()
    private var minLength = MutableLiveData<String>()
    private var minStars = MutableLiveData<Int>()


    fun fetchTrails() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // Update LiveData from IO dispatcher, use postValue
        var sort = if (currentSortIndex.value == null || (currentSortIndex.value)!!.toInt() == 0) "quality"
            else "distance"
        trails.postValue(trailRepository.getTrails(currentAddress.value.toString(), maxDistance.value.toString(),
            sort, minLength.value.toString(), minStars.value.toString()))
    }

    fun getTrails() : LiveData<List<Trail>> {
        return trails
    }

    internal fun getFavs(): MutableLiveData<MutableList<Trail>> {
        return favTrails
    }

    fun addFav(trail: Trail) {
        favTrails.value!!.add(trail)
        favTrails.postValue(favTrails.value!!)
        println(favTrails.value.toString())

        val currentFirebaseUser: FirebaseUser?  = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            database.child("users").child(currentFirebaseUser.uid).child("favTrails")
                .child(trail.id).setValue(trail)
        }
    }

    fun isFav(trailPost: Trail): Boolean {
        return favTrails.value?.contains(trailPost) ?: false
    }

    fun setFavs(newFavs: MutableList<Trail>) {
        favTrails.postValue(newFavs)
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
}