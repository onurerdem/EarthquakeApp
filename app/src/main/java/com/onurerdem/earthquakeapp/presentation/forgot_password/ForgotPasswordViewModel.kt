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
                    errorEmailText = "Please enter at least 3 characters."
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
                        Toast.makeText(context, "An e-mail has been sent for password reset.", Toast.LENGTH_LONG).show()
                        Toast.makeText(context, "Please check your spam or junk folder.", Toast.LENGTH_LONG).show()
                        forgotPasswordInProgress.value = false
                        navController.navigate(Screen.LoginScreen.route)
                    }
                }
                .await()
        } catch (e: Exception) {
            Log.d(TAG,"Inside_forgotPassword_failure")
            Log.d(TAG, "Exception= ${e.localizedMessage}")

            Toast.makeText(context, "${e.localizedMessage}", Toast.LENGTH_LONG).show()

            forgotPasswordInProgress.value = false
        }
    }
}