package com.onurerdem.earthquakeapp.presentation.earthquakes

sealed class EarthquakesEvent {
    data class Search(val searchString : String) : EarthquakesEvent()
}