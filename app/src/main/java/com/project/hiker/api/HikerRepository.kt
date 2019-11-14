package com.project.hiker.api

import android.util.Log

class HikerRepository(private val trailsApi: TrailsApi) {

    fun unpackTrails(response: TrailsApi.Trails): List<Trail>? {
        val trails: MutableList<Trail>? = mutableListOf()

        response.trails.forEach {
            val trail = it
            trails!!.add(trail)
        }

        return trails!!
    }

    fun getTrails(): List<Trail>? {
            val trails = trailsApi.getTrails().execute().body()
            return unpackTrails(trails!!)
    }

    fun getConditions(): Condition? {
        val conditions = trailsApi.getConditions().execute().body()
        Log.d("XXXXXXXXX",conditions.toString())
        return conditions
    }
}