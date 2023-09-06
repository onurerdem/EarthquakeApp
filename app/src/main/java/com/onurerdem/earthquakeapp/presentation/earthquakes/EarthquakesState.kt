package com.onurerdem.earthquakeapp.presentation.earthquakes

import com.onurerdem.earthquakeapp.domain.model.Earthquake

data class EarthquakesState(
    val isLoading : Boolean = false,
    val earthquakes : List<Earthquake> = emptyList(),
    val error : String = "",
    val search : String = ""
)