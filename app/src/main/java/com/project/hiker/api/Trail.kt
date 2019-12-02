package com.project.hiker.api

import com.google.gson.annotations.SerializedName

data class Trail (
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("stars")
    val stars: Double,
    @SerializedName("latitude")
    val latitude: Float,
    @SerializedName("longitude")
    val longitude: Float,
    @SerializedName("conditionDetails")
    val conditionDetails: String?,
    @SerializedName("conditionStatus")
    val conditionStatus: String?,
    @SerializedName("difficulty")
    val difficulty: String?
) {
    constructor() : this("", "",
        "", 0.0, 1.toFloat(),
        1.toFloat(), null, null, null
    )
}
