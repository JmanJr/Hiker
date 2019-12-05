package com.project.hiker.api

import com.google.gson.annotations.SerializedName

// trail object. Used for retrofit and lots of other goodies in a
// trail themed app
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
    @SerializedName("length")
    val length: Float,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("conditionDetails")
    val conditionDetails: String?,
    @SerializedName("conditionStatus")
    val conditionStatus: String?
) {
    constructor() : this("", "",
        "", 0.0, 1.toFloat(),
        1.toFloat(), 1.toFloat(), "", null, null
    )
}
