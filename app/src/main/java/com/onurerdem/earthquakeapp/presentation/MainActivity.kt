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
import com.onurerdem.earthquakeapp.presentation.earthquake_detail.views.EarthquakeDetailScreen
import com.onurerdem.earthquakeapp.presentation.earthquakes.views.EarthquakeScreen
import com.onurerdem.earthquakeapp.presentation.forgot_password.views.ForgotPasswordScreen
import com.onurerdem.earthquakeapp.presentation.login.views.LoginScreen
import com.onurerdem.earthquakeapp.presentation.onboarding.views.OnboardingScreen
import com.onurerdem.earthquakeapp.presentation.register.views.RegisterScreen
import com.onurerdem.earthquakeapp.presentation.register.views.TermsAndConditionsScreen
import com.onurerdem.earthquakeapp.presentation.splash.views.SplashScreen
import com.onurerdem.earthquakeapp.presentation.ui.theme.EarthquakeAppTheme
import com.onurerdem.earthquakeapp.util.Constants.EARTHQUAKE_ID
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
                    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
                        composable(route = Screen.SplashScreen.route) {
                            SplashScreen(navController = navController, context = this@MainActivity)
                        }

                        composable(route = Screen.OnboardingScreen.route) {
                            OnboardingScreen(navController = navController, context = this@MainActivity)
                        }

                        composable(route = Screen.RegisterScreen.route) {
                            RegisterScreen(navController = navController, context = this@MainActivity)
                        }

                        composable(route = Screen.TermsAndConditionsScreen.route) {
                            TermsAndConditionsScreen()
                        }

                        composable(route = Screen.LoginScreen.route) {
                            LoginScreen(navController = navController, context = this@MainActivity)
                        }

                        composable(route = Screen.ForgotPasswordScreen.route) {
                            ForgotPasswordScreen(navController = navController, context = this@MainActivity)
                        }

                        composable(route = Screen.EarthquakeScreen.route) {
                            EarthquakeScreen(navController = navController)
                        }

                        composable(route = Screen.EarthquakeDetailScreen.route + "/{${EARTHQUAKE_ID}}") {
                            EarthquakeDetailScreen()
                        }
                    }
                }
            }
        }
    }
}