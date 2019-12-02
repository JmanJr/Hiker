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

    suspend fun getTrails(address: String, maxDistance: String, sort: String, minLength: String, minStars: String): List<Trail>? {
        var trails: TrailsApi.Trails?
        var latLon: LatLon?
        try {
            latLon = latLonApi.getLatLon(address)
        } catch(e: Exception) {
            latLon = null
        }

        if (latLon != null) {
            val params: MutableMap<String, String> = HashMap()
            params.put("lat", latLon.getLat())
            params.put("lon", latLon.getLon())
            params.put("maxDistance", "10")
            params.put("key", "200620954-df710afab6f0931dab3f24fdd7754c1d")
            params.put("maxDistance", maxDistance)
            params.put("sort", sort)
            params.put("minLength", minLength)
            params.put("minStars", minStars)
            params.put("maxResults", "100")

            try {
                trails = trailsApi.getTrails(params).execute().body()
            } catch (e: Exception){
                trails = null
            }

        } else {
            trails = null
        }

        if (trails != null)
            return unpackTrails(trails)
        else
            return mutableListOf()
    }

    fun getConditions(): Condition? {
        val conditions = trailsApi.getConditions().execute().body()
        return conditions
    }
}