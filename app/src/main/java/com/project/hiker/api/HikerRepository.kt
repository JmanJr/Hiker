package com.project.hiker.api

class HikerRepository(private val trailsApi: TrailsApi, private val latLonApi: LatLonApi) {

    // turns a trails from into a list of trails
    fun unpackTrails(response: TrailsApi.Trails): List<Trail>? {
        val trails: MutableList<Trail>? = mutableListOf()

        response.trails.forEach {
            val trail = it
            trails!!.add(trail)
        }

        return trails!!
    }

    // gets trails from API. Packs all query params into a map,
    // then passes along request. Then turns response into trail data
    suspend fun getTrails(address: String, maxDistance: String, sort: String, minLength: String,
                          minStars: String): List<Trail>? {
        var trails: TrailsApi.Trails?
        var latLon: LatLon?
        try {
            latLon = latLonApi.getLatLon(address)
        } catch(e: Exception) {
            latLon = null
        }

        // lat lon is necessary. If no lat lon... no trails
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

            // if service error just have empty list
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

    // gets specific trails based on ID. Used for favorites.
    suspend fun getTrailsByIds(ids: String): MutableList<Trail>? {
        var trails: TrailsApi.Trails?

        // better be some IDs in here or you got no favs
        if (ids != "") {
            val params: MutableMap<String, String> = HashMap()
            params.put("key", "200620954-df710afab6f0931dab3f24fdd7754c1d")
            params.put("ids", ids)

            try {
                trails = trailsApi.getTrailsByIds(params).execute().body()
            } catch (e: Exception){
                trails = null
            }

        } else {
            trails = null
        }

        if (trails != null)
            return unpackTrails(trails)!!.toMutableList()
        else
            return mutableListOf()
    }
}