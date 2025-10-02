package com.onurerdem.earthquakeapp.domain.model

data class Earthquake(
    val date: String?,
    val depth: Double,
    val earthquake_id: String,
    val mag: Double,
    val title: String
)
