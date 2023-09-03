package com.onurerdem.earthquakeapp.data.remote

import com.onurerdem.earthquakeapp.data.remote.dto.EarthquakeDetailDto
import com.onurerdem.earthquakeapp.data.remote.dto.EarthquakesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface EarthquakeAPI {
    @GET("deprem")
    suspend fun getEarthquakes() : EarthquakesDto

    @GET("deprem/data/get/")
    suspend fun getEarthquakeDetail(
        @Query("earthquake_id") earthquakeId : String
    ) : EarthquakeDetailDto
}