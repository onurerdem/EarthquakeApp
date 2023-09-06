package com.onurerdem.earthquakeapp.presentation.earthquake_detail

import com.onurerdem.earthquakeapp.domain.model.EarthquakeDetail

data class EarthquakeDetailState (
    val isLoading : Boolean = false,
    val earthquake : EarthquakeDetail? = null,
    val error : String = ""
)