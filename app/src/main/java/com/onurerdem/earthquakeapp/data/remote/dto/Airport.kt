package com.onurerdem.earthquakeapp.data.remote.dto

data class Airport(
    val code: String,
    val coordinates: Coordinates,
    val distance: Double,
    val name: String
)