package com.onurerdem.earthquakeapp.domain.model

import com.onurerdem.earthquakeapp.data.remote.dto.AirportX
import com.onurerdem.earthquakeapp.data.remote.dto.ClosestCityX
import com.onurerdem.earthquakeapp.data.remote.dto.GeojsonX

data class EarthquakeDetail (
    val date: String?,
    val depth: Double,
    val geojson: GeojsonX,
    val closestCities: List<ClosestCityX>,
    val airports: List<AirportX>,
    val population: Int,
    val mag: Double,
    val provider: String,
    val title: String
)