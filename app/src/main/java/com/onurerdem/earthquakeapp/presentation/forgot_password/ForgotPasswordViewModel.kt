package com.onurerdem.earthquakeapp.presentation.forgot_password

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.Validator
import kotlinx.coroutines.tasks.await

class ForgotPasswordViewModel(): ViewModel() {

    private val TAG = ForgotPasswordViewModel::class.simpleName

    var forgotPasswordState = mutableStateOf(ForgotPasswordState())

    var allValidationsPassed = mutableStateOf(false)

    var forgotPasswordInProgress = mutableStateOf(false)

    var errorEmailText = ""

    suspend fun onEvent(event: ForgotPasswordEvent, navController: NavController, context: Context) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> {
                forgotPasswordState.value = forgotPasswordState.value.copy(
                    email = event.email
                )

                if (event.email.length >= 3 && !event.email.isNullOrEmpty() && !event.email.isNullOrBlank()) {
                    errorEmailText = ""
                } else {
                    errorEmailText = "Lütfen en az 3 karakter girin."
                }
            }

            is ForgotPasswordEvent.ResetPasswordButtonClicked -> {
                resetPassword(navController, context)
            }
        }
        validateForgotPasswordUIDataWithRules()
    }

    private fun validateForgotPasswordUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = forgotPasswordState.value.email
        )

        forgotPasswordState.value = forgotPasswordState.value.copy(
            emailError = emailResult.status,
        )

        allValidationsPassed.value = emailResult.status

    }

    private suspend fun resetPassword(navController: NavController, context: Context) {

        forgotPasswordInProgress.value = true
        val email = forgotPasswordState.value.email

        try {
            FirebaseAuth
                .getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    Log.d(TAG,"Inside_resetPassword_success")
                    Log.d(TAG,"${it.isSuccessful}")

                    if(it.isSuccessful) {
                        Toast.makeText(context, "Şifre yenileme işlemi için e-mail gönderildi.", Toast.LENGTH_LONG).show()
                        Toast.makeText(context, "Lütfen spam veya gereksiz klasörünüzü kontrol edin.", Toast.LENGTH_LONG).show()
                        forgotPasswordInProgress.value = false
                        navController.navigate(Screen.LoginScreen.route)
                    }
                }
                .await()
        } catch (e: Exception) {
            Log.d(TAG,"Inside_forgotPassword_failure")
            Log.d(TAG, "Exception= ${e.localizedMessage}")
            if ("${e.localizedMessage}" == "The email address is badly formatted.") {
                Toast.makeText(context, "Lütfen e-posta adresinizi düzgün giriniz.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Şifre yenileme başarısız.", Toast.LENGTH_LONG).show()
            }

            forgotPasswordInProgress.value = false
        }
    }
}