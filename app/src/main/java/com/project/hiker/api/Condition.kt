package com.project.hiker.api

import com.google.gson.annotations.SerializedName

data class Condition (
    @SerializedName("conditionStatus")
    val conditionStatus: String,
    @SerializedName("conditionColor")
    val conditionColor: String,
    @SerializedName("conditionDetails")
    val conditionDetails: String
)
