package com.project.hiker.ui

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
    private val trails = MutableLiveData<List<Trail>>()

    fun fetchTrails() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // Update LiveData from IO dispatcher, use postValue
        trails.postValue(trailRepository.getTrails())
    }

    fun getTrails() : LiveData<List<Trail>> {
        return trails
    }
}