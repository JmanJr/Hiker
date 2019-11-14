package com.project.hiker.api

import android.util.Log

class HikerRepository(private val hikerApi: HikerApi) {

    fun unpackTrails(response: HikerApi.Trails): List<Trail>? {
        val trails: MutableList<Trail>? = mutableListOf()

        response.trails.forEach {
            val trail = it
            trails!!.add(trail)
        }

        return trails!!
    }

    fun getTrails(): List<Trail>? {
            val trails = hikerApi.getTrails().execute().body()
            return unpackTrails(trails!!)
    }
}