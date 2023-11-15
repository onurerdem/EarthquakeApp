package com.onurerdem.earthquakeapp.presentation.register.views

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.presentation.AlertDialogExample
import com.onurerdem.earthquakeapp.presentation.ButtonComponent
import com.onurerdem.earthquakeapp.presentation.CheckboxComponent
import com.onurerdem.earthquakeapp.presentation.ClickableLoginTextComponent
import com.onurerdem.earthquakeapp.presentation.DividerTextComponent
import com.onurerdem.earthquakeapp.presentation.ErrorTextComponent
import com.onurerdem.earthquakeapp.presentation.HeadingTextComponent
import com.onurerdem.earthquakeapp.presentation.MainActivity
import com.onurerdem.earthquakeapp.presentation.MyTextFieldComponent
import com.onurerdem.earthquakeapp.presentation.NormalTextComponent
import com.onurerdem.earthquakeapp.presentation.PasswordTextFieldComponent
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.isDarkThemeMode
import com.onurerdem.earthquakeapp.presentation.register.RegisterEvent
import com.onurerdem.earthquakeapp.presentation.register.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel(),
    context: MainActivity
) {

    val openAlertDialog = remember { mutableStateOf(false) }

    val activity = (LocalContext.current as? Activity)

    when {
        openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    activity?.finish()
                },
                dialogTitle = "Çıkış",
                dialogText = "Çıkmak istediğinize emin misiniz?",
                icon = Icons.Default.ExitToApp,
                iconContentColor = Color.Red,
                confirmButtonText = "Evet",
                dismissButtonText = "Hayır",
                dismissButtonColor = Color.Red,
                confirmButtonIcon = null,
                dismissButtonIcon = null,
                context = context
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkThemeMode(context = context)) Color.DarkGray else Color.White)
                .padding(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(if (isDarkThemeMode(context = context)) Color.DarkGray else Color.White)
            ) {

                NormalTextComponent(value = "Merhaba,", context = context)
                HeadingTextComponent(value = "Kayıt ol.", context = context)
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = "Ad",
                    painterResource(id = R.drawable.baseline_person_outline_24),
                    onTextChanged = {
                        registerViewModel.onEvent(
                            RegisterEvent.FirstNameChanged(it),
                            navController,
                            context = context
                        )
                    },
                    errorStatus = registerViewModel.registerState.value.firstNameError,
                    screen = Screen.RegisterScreen,
                    context = context
                )

                if (!registerViewModel.errorFirstNameText.isNullOrEmpty() && !registerViewModel.errorFirstNameText.isNullOrBlank() && registerViewModel.errorFirstNameText != "") {
                    ErrorTextComponent(value = registerViewModel.errorFirstNameText)
                }

                MyTextFieldComponent(
                    labelValue = "Soyad",
                    painterResource = painterResource(id = R.drawable.baseline_person_outline_24),
                    onTextChanged = {
                        registerViewModel.onEvent(
                            RegisterEvent.LastNameChanged(it),
                            navController,
                            context
                        )
                    },
                    errorStatus = registerViewModel.registerState.value.lastNameError,
                    screen = Screen.RegisterScreen,
                    context = context
                )

                if (!registerViewModel.errorLastNameText.isNullOrEmpty() && !registerViewModel.errorLastNameText.isNullOrBlank() && registerViewModel.errorLastNameText != "") {
                    ErrorTextComponent(value = registerViewModel.errorLastNameText)
                }

                MyTextFieldComponent(
                    labelValue = "Email",
                    painterResource = painterResource(id = R.drawable.baseline_mail_outline_24),
                    onTextChanged = {
                        registerViewModel.onEvent(
                            RegisterEvent.EmailChanged(it),
                            navController,
                            context
                        )
                    },
                    errorStatus = registerViewModel.registerState.value.emailError,
                    screen = Screen.RegisterScreen,
                    context = context
                )

                if (!registerViewModel.errorEmailText.isNullOrEmpty() && !registerViewModel.errorEmailText.isNullOrBlank() && registerViewModel.errorEmailText != "") {
                    ErrorTextComponent(value = registerViewModel.errorEmailText)
                }

                PasswordTextFieldComponent(
                    labelValue = "Şifre",
                    painterResource = painterResource(id = R.drawable.outline_lock_24),
                    onTextSelected = {
                        registerViewModel.onEvent(
                            RegisterEvent.PasswordChanged(it),
                            navController,
                            context
                        )
                    },
                    errorStatus = registerViewModel.registerState.value.passwordError,
                    context = context
                )

                if (!registerViewModel.errorPasswordText.isNullOrEmpty() && !registerViewModel.errorPasswordText.isNullOrBlank() && registerViewModel.errorPasswordText != "") {
                    ErrorTextComponent(value = registerViewModel.errorPasswordText)
                }

                CheckboxComponent(
                    onTextSelected = {
                        navController.navigate(Screen.TermsAndConditionsScreen.route)
                    },
                    onCheckedChange = {
                        registerViewModel.onEvent(
                            RegisterEvent.PrivacyPolicyCheckBoxClicked(it),
                            navController,
                            context
                        )
                    },
                    context = context
                )

                if (!registerViewModel.errorPrivacyPolicyCheckBoxClickedText.isNullOrEmpty() && !registerViewModel.errorPrivacyPolicyCheckBoxClickedText.isNullOrBlank() && registerViewModel.errorPrivacyPolicyCheckBoxClickedText != "") {
                    ErrorTextComponent(value = registerViewModel.errorPrivacyPolicyCheckBoxClickedText)
                }

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = "Kayıt ol",
                    onButtonClicked = {
                        registerViewModel.onEvent(
                            RegisterEvent.RegisterButtonClicked,
                            navController,
                            context
                        )
                    },
                    isEnabled = registerViewModel.allValidationsPassed.value
                )

                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent(context = context)

                ClickableLoginTextComponent(
                    tryingToLogin = true,
                    onTextSelected = {
                        navController.navigate(Screen.LoginScreen.route)
                    },
                    context = context
                )
            }

        }

        if (registerViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }

    BackHandler {
        openAlertDialog.value = !openAlertDialog.value
    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen() {
    RegisterScreen(navController = rememberNavController(), context = MainActivity())
}