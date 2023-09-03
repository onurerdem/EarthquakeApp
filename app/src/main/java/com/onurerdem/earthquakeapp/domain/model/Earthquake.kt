package com.onurerdem.earthquakeapp.domain.model

import com.onurerdem.earthquakeapp.data.remote.dto.Geojson
import com.onurerdem.earthquakeapp.data.remote.dto.LocationProperties

data class Earthquake(
    val date: String,
    val depth: Double,
    val earthquake_id: String,
    val mag: Double,
    val title: String
)
