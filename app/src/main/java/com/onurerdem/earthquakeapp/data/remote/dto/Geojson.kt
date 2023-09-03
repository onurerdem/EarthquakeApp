package com.onurerdem.earthquakeapp.data.remote.dto

data class Geojson(
    val coordinates: List<Double>,
    val type: String
)