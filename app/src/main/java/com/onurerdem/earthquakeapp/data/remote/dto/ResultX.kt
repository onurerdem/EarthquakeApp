package com.onurerdem.earthquakeapp.data.remote.dto

data class ResultX(
    val _id: String,
    val created_at: Int,
    val date: String,
    val date_time: String,
    val depth: Double,
    val earthquake_id: String,
    val geojson: GeojsonX,
    val location_properties: LocationPropertiesX,
    val location_tz: String,
    val mag: Double,
    val provider: String,
    val rev: Any,
    val title: String
)