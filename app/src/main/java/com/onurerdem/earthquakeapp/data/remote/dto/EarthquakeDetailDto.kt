package com.onurerdem.earthquakeapp.data.remote.dto

import com.onurerdem.earthquakeapp.domain.model.EarthquakeDetail

data class EarthquakeDetailDto(
    val desc: String,
    val httpStatus: Int,
    val result: ResultX,
    val serverloadms: Int,
    val status: Boolean
)

fun EarthquakeDetailDto.toEarthquakeDetail() : EarthquakeDetail {
    return EarthquakeDetail(result.date, result.depth, result.geojson, result.location_properties.closestCities, result.location_properties.airports, result.location_properties.epiCenter.population, result.mag, result.provider, result.title)
}