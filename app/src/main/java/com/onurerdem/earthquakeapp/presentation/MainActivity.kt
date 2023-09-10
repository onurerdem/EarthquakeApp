package com.onurerdem.earthquakeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onurerdem.earthquakeapp.presentation.earthquakes.views.EarthquakeScreen
import com.onurerdem.earthquakeapp.presentation.ui.theme.EarthquakeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EarthquakeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.EarthquakeScreen.route) {
                        composable(route = Screen.EarthquakeScreen.route) {
                            EarthquakeScreen(navController = navController)
                        }

                        composable(route = Screen.EarthquakeDetailScreen.route) {

                        }
                    }
                }
            }
        }
    }
}