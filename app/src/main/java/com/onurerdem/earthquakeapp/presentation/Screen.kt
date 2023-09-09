package com.onurerdem.earthquakeapp.presentation

sealed class Screen(val route: String) {
    object EarthquakeScreen : Screen("earthquake_screen")
    object EarthquakeDetailScreen : Screen("earthquake_detail_screen")
}