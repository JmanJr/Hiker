package com.project.hiker.api

import com.google.gson.annotations.SerializedName

data class LatLon (
    @SerializedName("latt")
    val latitude: Float,
    @SerializedName("longt")
    val longitude: Float
) {
    fun getLat(): String {
        return latitude.toString()
    }

    fun getLon(): String {
        return longitude.toString()
    }
}

