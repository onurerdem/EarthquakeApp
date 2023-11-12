package com.onurerdem.earthquakeapp.presentation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object OnboardingScreen : Screen("onboarding_screen")
    object RegisterScreen : Screen("register_screen")
    object TermsAndConditionsScreen : Screen("terms_and_conditions_screen")
    object LoginScreen : Screen("login_screen")
    object ForgotPasswordScreen : Screen("forgot_password_screen")
    object EarthquakeScreen : Screen("earthquake_screen")
    object EarthquakeDetailScreen : Screen("earthquake_detail_screen")
}