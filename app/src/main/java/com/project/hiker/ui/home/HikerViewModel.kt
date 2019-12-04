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

class HikerViewModel: ViewModel() {
    private val weatherApi = WeatherApi.create()
    private val trailsApi = TrailsApi.create()
    private val latLonApi = LatLonApi.create()
    private val trailRepository = HikerRepository(trailsApi, latLonApi)
    private var trails = MutableLiveData<List<Trail>>().apply {
        value = mutableListOf()
    }
    //private var weathers = MutableLiveData<List<WeatherObj>>().apply {
      //  value = mutableListOf()
    //}


    lateinit var weatherList: List<WeatherObj>
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

    fun fetchFavTrails(ids: String) = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // Update LiveData from IO dispatcher, use postValue
        favTrails.postValue(trailRepository.getTrailsByIds(ids))
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
                .child(trail.id).setValue(trail.id)
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

    // calls the companion class's doOnePost method
    fun viewTrail(context: Context, trail: Trail) {
        Companion.viewTrail(context, trail)
    }

    // Convenient place to put it as it is shared
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
            ContextCompat.startActivity(context, intent, null)
        }
    }





    fun unpackWeather(response: WeatherApi.Weathers?): List<WeatherObj> {
        val weathers: MutableList<WeatherObj>? = mutableListOf()

        response?.weathers?.forEach {
            val weather = it
            weathers!!.add(weather)
            System.out.println("weather is getting set")
        }

        return weathers!!
    }



    private fun setWeathers(lat: Float, long: Float) {
        var weathers: WeatherApi.Weathers?

        val params: MutableMap<String, String> = HashMap()
        params.put("lat", lat.toString())
        params.put("lon", long.toString())
        params.put("cnt", "10")
        params.put("appid", "b1b15e88fa797225412429c1c50c122a1")

        weathers = weatherApi.getWeathers(lat.toString(), long.toString(),"10","b1b15e88fa797225412429c1c50c122a1").execute().body()

        weatherList = unpackWeather(weathers)
        println("the count of weathers List: " + weathers?.weathers)

    }

    fun fetchWeathers(lat: Float, long: Float) = viewModelScope.launch(
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        // Update LiveData from IO dispatcher, use postValue
        setWeathers(lat, long)
    }

    fun getWeathers(): List<WeatherObj> {
        return weatherList
    }

//    fun gotToWeather(context: Context, trail: Trail) {
//        Companion.goToWeather(context, trail)
//    }

//    @GlideExtension
//    companion object {
//        fun goToWeather(context: Context, trail: Trail) {
//            val intent = Intent(context, Weather::class.java)
//            intent.putExtra("id", trail.id)
//            intent.putExtra("lon", trail.longitude)
//            intent.putExtra("lat", trail.latitude)
//            intent.putExtra("name", trail.name)
//            ContextCompat.startActivity(context, intent, null)
//        }
//    }
}