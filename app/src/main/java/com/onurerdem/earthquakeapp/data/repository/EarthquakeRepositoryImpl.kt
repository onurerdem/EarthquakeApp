package com.onurerdem.earthquakeapp.data.repository

import com.onurerdem.earthquakeapp.data.remote.EarthquakeAPI
import com.onurerdem.earthquakeapp.data.remote.dto.EarthquakeDetailDto
import com.onurerdem.earthquakeapp.data.remote.dto.EarthquakesDto
import com.onurerdem.earthquakeapp.domain.repository.EarthquakeRepository
import javax.inject.Inject

class EarthquakeRepositoryImpl @Inject constructor(private val api: EarthquakeAPI) : EarthquakeRepository {
    override suspend fun getEarthquakes(): EarthquakesDto {
        return api.getEarthquakes()
    }

    override suspend fun getEarthquakeDetail(earthquakeId: String) : EarthquakeDetailDto {
        return api.getEarthquakeDetail(earthquakeId = earthquakeId)
    }
}