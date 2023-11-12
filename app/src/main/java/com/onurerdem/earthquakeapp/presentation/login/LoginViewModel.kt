package com.onurerdem.earthquakeapp.presentation.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.Validator

class LoginViewModel() : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginState = mutableStateOf(LoginState())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)

    var errorEmailText = ""
    var errorPasswordText = ""

    fun onEvent(event: LoginEvent, navController: NavController, context: Context) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                loginState.value = loginState.value.copy(
                    email = event.email
                )

                if (event.email.length >= 3 && !event.email.isNullOrEmpty() && !event.email.isNullOrBlank()) {
                    errorEmailText = ""
                } else {
                    errorEmailText = "Lütfen en az 3 karakter girin."
                }
            }

            is LoginEvent.PasswordChanged -> {
                loginState.value = loginState.value.copy(
                    password = event.password
                )

                if (event.password.length >= 4 && !event.password.isNullOrEmpty() && !event.password.isNullOrBlank()) {
                    errorPasswordText = ""
                } else {
                    errorPasswordText = "Lütfen en az 4 karakter girin."
                }
            }

            is LoginEvent.LoginButtonClicked -> {
                login(navController, context)
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = loginState.value.password
        )

        loginState.value = loginState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status

    }

    private fun login(navController: NavController, context: Context) {

        loginInProgress.value = true
        val email = loginState.value.email
        val password = loginState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG,"Inside_login_success")
                Log.d(TAG,"${it.isSuccessful}")

                if(it.isSuccessful){
                    if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                        Toast.makeText(context, "Giriş başarılı.", Toast.LENGTH_LONG).show()
                        navController.navigate(Screen.EarthquakeScreen.route)
                    } else {
                        Toast.makeText(context, "E-postanız doğrulanmadı. Lütfen e-postanızı doğrulayın.", Toast.LENGTH_LONG).show()
                        Toast.makeText(context, "Lütfen spam veya gereksiz klasörünüzü kontrol edin.", Toast.LENGTH_LONG).show()
                    }
                    loginInProgress.value = false
                }
            }
            .addOnFailureListener {
                Log.d(TAG,"Inside_login_failure")
                Log.d(TAG, "Exception= ${it.localizedMessage}")
                if ("${it.localizedMessage}" == "The email address is badly formatted.") {
                    Toast.makeText(context, "Lütfen e-posta adresinizi düzgün giriniz.", Toast.LENGTH_LONG).show()
                } else if ("${it.localizedMessage}" == "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]") {
                    Toast.makeText(context, "Geçersiz e-posta veya şifre.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Giriş başarısız.", Toast.LENGTH_LONG).show()
                }

                loginInProgress.value = false
            }
    }
}