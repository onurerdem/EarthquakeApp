package com.onurerdem.earthquakeapp.data.remote.dto

data class ClosestCity(
    val cityCode: Int,
    val distance: Double,
    val name: String,
    val population: Int
)