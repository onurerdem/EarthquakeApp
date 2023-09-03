package com.onurerdem.earthquakeapp.data.remote.dto

data class LocationPropertiesX(
    val airports: List<AirportX>,
    val closestCities: List<ClosestCityX>,
    val closestCity: ClosestCityX,
    val epiCenter: EpiCenterX
)