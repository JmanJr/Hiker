package com.project.hiker.ui.home

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.*
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

    private var favTrails = MutableLiveData<MutableList<Trail>>().apply {
        value = mutableListOf()
    }

    fun fetchTrails() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // Update LiveData from IO dispatcher, use postValue
        trails.postValue(trailRepository.getTrails(currentAddress.value.toString()))
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
    }

    fun isFav(trailPost: Trail): Boolean {
        return favTrails.value?.contains(trailPost) ?: false
    }

    fun removeFav(trail: Trail) {
        favTrails.value!!.remove(trail)
        favTrails.postValue(favTrails.value!!)
    }
    
    fun getAddress(): LiveData<String> {
        return currentAddress
    }

    fun setAddress(newAddress: String) {
        currentAddress.postValue(newAddress)
    }
}