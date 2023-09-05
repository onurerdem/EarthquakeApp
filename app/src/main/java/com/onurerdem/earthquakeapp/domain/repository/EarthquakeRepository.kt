package com.onurerdem.earthquakeapp.domain.repository

import com.onurerdem.earthquakeapp.data.remote.dto.EarthquakeDetailDto
import com.onurerdem.earthquakeapp.data.remote.dto.EarthquakesDto

interface EarthquakeRepository {
    suspend fun getEarthquakes() : EarthquakesDto
    suspend fun getEarthquakeDetail(earthquakeId : String) : EarthquakeDetailDto
}