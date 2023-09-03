package com.onurerdem.earthquakeapp.data.remote.dto

data class LocationProperties(
    val airports: List<Airport>,
    val closestCities: List<ClosestCity>,
    val closestCity: ClosestCity,
    val epiCenter: EpiCenter
)