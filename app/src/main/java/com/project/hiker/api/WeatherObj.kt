package com.project.hiker.api

import com.google.gson.annotations.SerializedName

data class WeatherObj (
    @SerializedName("cod")
    val cod: String
//    @SerializedName("id")
//    val id: String,
//    @SerializedName("description")
//    val description: String,
//    @SerializedName("speed")
//    val speed: Float,
//    @SerializedName("deg")
//    val deg: Float,
//    @SerializedName("clouds")
//    val clouds: Int,
//    @SerializedName("humidity")
//    val humidity: Int

)
//{
//    constructor() : this("", "",
//        "", 0.toFloat(), 0.toFloat(),
//        0, 0
//    )
//}
{
    constructor() : this(
        ""
    )
}