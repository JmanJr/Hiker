package com.project.hiker.api

import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TrailsApi {

    @GET("/data/get-trails?lat=40.0274&lon=-105.2519&maxDistance=10&key=200620954-df710afab6f0931dab3f24fdd7754c1d")
    fun getTrails(): Call<Trails>

    @GET("/data/get-conditions?ids=7001635&key=200620954-df710afab6f0931dab3f24fdd7754c1d")
    fun getConditions(): Call<Condition>

    class Trails(
        val trails: List<Trail>
    )

    companion object {
        private fun buildGsonConverterFactory(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder()
            return GsonConverterFactory.create(gsonBuilder.create())
        }
        var httpurl = HttpUrl.Builder()
            .scheme("https")
            .host("www.hikingproject.com")
            .build()
        fun create(): TrailsApi = create(httpurl)
        private fun create(httpUrl: HttpUrl): TrailsApi {

            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(buildGsonConverterFactory())
                .build()
                .create(TrailsApi::class.java)
        }
    }
}