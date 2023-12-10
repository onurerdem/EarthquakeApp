package com.onurerdem.earthquakeapp.presentation.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.Validator

class RegisterViewModel : ViewModel() {

    private val TAG = RegisterViewModel::class.simpleName


    var registerState = mutableStateOf(RegisterState())

    var allValidationsPassed = mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false)

    var errorFirstNameText = ""
    var errorLastNameText = ""
    var errorEmailText = ""
    var errorPasswordText = ""
    var errorPrivacyPolicyCheckBoxClickedText = ""

    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    val dataMap = hashMapOf<String, String>()

    fun onEvent(event: RegisterEvent, navController: NavController, context: Context) {
        when (event) {
            is RegisterEvent.FirstNameChanged -> {
                registerState.value = registerState.value.copy(
                    firstName = event.firstName
                )
                printState()

                if (event.firstName.length >= 2 && !event.firstName.isNullOrEmpty() && !event.firstName.isNullOrBlank()) {
                    errorFirstNameText = ""
                } else {
                    errorFirstNameText = "Please enter at least 2 characters."
                }
            }

            is RegisterEvent.LastNameChanged -> {
                registerState.value = registerState.value.copy(
                    lastName = event.lastName
                )
                printState()

                if (event.lastName.length >= 2 && !event.lastName.isNullOrEmpty() && !event.lastName.isNullOrBlank()) {
                    errorLastNameText = ""
                } else {
                    errorLastNameText = "Please enter at least 2 characters."
                }
            }

            is RegisterEvent.EmailChanged -> {
                registerState.value = registerState.value.copy(
                    email = event.email
                )
                printState()

                if (event.email.length >= 3 && !event.email.isNullOrEmpty() && !event.email.isNullOrBlank()) {
                    errorEmailText = ""
                } else {
                    errorEmailText = "Please enter at least 3 characters."
                }
            }

            is RegisterEvent.PasswordChanged -> {
                registerState.value = registerState.value.copy(
                    password = event.password
                )
                printState()

                if (event.password.length >= 4 && !event.password.isNullOrEmpty() && !event.password.isNullOrBlank()) {
                    errorPasswordText = ""
                } else {
                    errorPasswordText = "Please enter at least 4 characters."
                }
            }

            is RegisterEvent.RegisterButtonClicked -> {
                signUp(navController, context)
            }

            is RegisterEvent.PrivacyPolicyCheckBoxClicked -> {
                registerState.value = registerState.value.copy(
                    privacyPolicyAccepted = event.status
                )

                if (event.status == true) {
                    errorPrivacyPolicyCheckBoxClickedText = ""
                } else {
                    errorPrivacyPolicyCheckBoxClickedText = "Please accept it."
                }
            }
        }
        validateDataWithRules()
    }

    private fun signUp(navController: NavController, context: Context) {
        Log.d(TAG, "Inside_signUp")
        printState()
        createUserInFirebase(
            email = registerState.value.email,
            password = registerState.value.password,
            navController = navController,
            context = context
        )
    }

    private fun validateDataWithRules() {
        val fNameResult = Validator.validateFirstName(
            fName = registerState.value.firstName
        )

        val lNameResult = Validator.validateLastName(
            lName = registerState.value.lastName
        )

        val emailResult = Validator.validateEmail(
            email = registerState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = registerState.value.password
        )

        val privacyPolicyResult = Validator.validatePrivacyPolicyAcceptance(
            statusValue = registerState.value.privacyPolicyAccepted
        )


        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "fNameResult= $fNameResult")
        Log.d(TAG, "lNameResult= $lNameResult")
        Log.d(TAG, "emailResult= $emailResult")
        Log.d(TAG, "passwordResult= $passwordResult")
        Log.d(TAG, "privacyPolicyResult= $privacyPolicyResult")

        registerState.value = registerState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
            privacyPolicyError = privacyPolicyResult.status
        )


        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status && privacyPolicyResult.status

    }


    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, registerState.value.toString())
    }


    private fun createUserInFirebase(email: String, password: String, navController: NavController, context: Context) {

        signUpInProgress.value = true

        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_OnCompleteListener")
                Log.d(TAG, " isSuccessful = ${it.isSuccessful}")

                signUpInProgress.value = false
                if (it.isSuccessful) {
                    FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                    Toast.makeText(context, "Registration Successful. We have sent you an email to verify your email.", Toast.LENGTH_LONG).show()
                    Toast.makeText(context, "Please check your spam or junk folder.", Toast.LENGTH_LONG).show()
                    val currentUser = auth.currentUser?.email.toString()
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.LoginScreen.route)

                    val token = FirebaseMessaging.getInstance().token.addOnSuccessListener {
                        dataMap.put("token",it)
                        dataMap.put("useremail", currentUser)

                        db.collection("Users").add(dataMap).addOnSuccessListener {
                            //do some stuff
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_OnFailureListener")
                Log.d(TAG, "Exception= ${it.message}")
                Log.d(TAG, "Exception= ${it.localizedMessage}")

                Toast.makeText(context, "${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }
}