package com.project.hiker.ui

import androidx.lifecycle.MutableLiveData
import com.project.hiker.api.TrailsApi
import com.project.hiker.api.Trail
import com.project.hiker.api.HikerRepository
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.*
import com.project.hiker.api.Condition
import kotlinx.coroutines.launch

class HikerViewModel: ViewModel() {

    private val hikerApi = TrailsApi.create()
    private val hikerRepository = HikerRepository(hikerApi)
    private val trails = MutableLiveData<List<Trail>>()
    private val conditions = MutableLiveData<Condition>()

    fun fetchTrails() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // Update LiveData from IO dispatcher, use postValue
        trails.postValue(hikerRepository.getTrails())
    }

    fun getTrails() : LiveData<List<Trail>> {
        return trails
    }

    fun fetchConditions() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // Update LiveData from IO dispatcher, use postValue
        conditions.postValue(hikerRepository.getConditions())
    }

    fun getConditions() : LiveData<Condition> {
        return conditions
    }
}