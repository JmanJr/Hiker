package com.project.hiker.api

import android.util.Log

class HikerRepository(private val trailsApi: TrailsApi, private val latLonApi: LatLonApi) {

    fun unpackTrails(response: TrailsApi.Trails): List<Trail>? {
        val trails: MutableList<Trail>? = mutableListOf()

        response.trails.forEach {
            val trail = it
            trails!!.add(trail)
        }

        return trails!!
    }

    suspend fun getTrails(address: String): List<Trail>? {
        val latLon = latLonApi.getLatLon(address)
        Log.d("LAT", latLon.getLat())
        Log.d("LON", latLon.getLon())
        val params: MutableMap<String, String> = HashMap<String, String>()
        params.put("lat", latLon.getLat())
        params.put("lon", latLon.getLon())
        params.put("maxDistance", "10")
        params.put("key", "200620954-df710afab6f0931dab3f24fdd7754c1d")

        val trails = trailsApi.getTrails(params).execute().body()
        return unpackTrails(trails!!)
    }

    fun getConditions(): Condition? {
        val conditions = trailsApi.getConditions().execute().body()
        return conditions
    }
}