package com.onurerdem.earthquakeapp.presentation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object EarthquakeScreen : Screen("earthquake_screen")
    object EarthquakeDetailScreen : Screen("earthquake_detail_screen")
}