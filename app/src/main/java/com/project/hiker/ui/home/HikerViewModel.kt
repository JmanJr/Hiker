package com.project.hiker.ui.home

import androidx.lifecycle.MutableLiveData
import com.project.hiker.api.HikerApi
import com.project.hiker.api.Trail
import com.project.hiker.api.HikerRepository
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class HikerViewModel: ViewModel() {

    private val hikerApi = HikerApi.create()
    private val trailRepository = HikerRepository(hikerApi)
    private var trails = MutableLiveData<List<Trail>>().apply {
        value = mutableListOf()
    }

    private var favTrails = MutableLiveData<List<Trail>>().apply {
        value = mutableListOf()
    }

    fun fetchTrails() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // Update LiveData from IO dispatcher, use postValue
        trails.postValue(trailRepository.getTrails())
    }

    fun getTrails() : LiveData<List<Trail>> {
        return trails
    }

    internal fun getFavs(): LiveData<List<Trail>> {
        return favTrails
    }

    fun addFav(trail: Trail) {
        val localList = favTrails.value?.toMutableList()
        localList?.let {
            it.add(trail)
            favTrails.value = it
        }
    }

    fun isFav(trailPost: Trail): Boolean {
        return favTrails.value?.contains(trailPost) ?: false
    }

    fun removeFav(trail: Trail) {
        val localList = favTrails.value?.toMutableList()
        localList?.let{
            it.remove(trail)
            favTrails.value = it
        }
    }
}