package com.onurerdem.earthquakeapp.data.remote.dto

import com.onurerdem.earthquakeapp.domain.model.Earthquake

data class EarthquakesDto(
    val desc: String,
    val httpStatus: Int,
    val metadata: Metadata,
    val result: List<Result>,
    val serverloadms: Int,
    val status: Boolean
)

fun EarthquakesDto.toEarthquakeList(): List<Earthquake> {
    return result.map { result -> Earthquake(result.date, result.depth, result.earthquake_id, result.mag, result.title) }
}