package com.onurerdem.earthquakeapp.presentation.login.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.presentation.ButtonComponent
import com.onurerdem.earthquakeapp.presentation.ClickableLoginTextComponent
import com.onurerdem.earthquakeapp.presentation.DividerTextComponent
import com.onurerdem.earthquakeapp.presentation.ErrorTextComponent
import com.onurerdem.earthquakeapp.presentation.HeadingTextComponent
import com.onurerdem.earthquakeapp.presentation.MainActivity
import com.onurerdem.earthquakeapp.presentation.MyTextFieldComponent
import com.onurerdem.earthquakeapp.presentation.NormalTextComponent
import com.onurerdem.earthquakeapp.presentation.PasswordTextFieldComponent
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.ClickableUnderLinedTextComponent
import com.onurerdem.earthquakeapp.presentation.UIText
import com.onurerdem.earthquakeapp.presentation.isDarkThemeMode
import com.onurerdem.earthquakeapp.presentation.login.LoginEvent
import com.onurerdem.earthquakeapp.presentation.login.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(),
    context: MainActivity
) {

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
                    .background(if (isDarkThemeMode(context = context)) Color.DarkGray else Color.White)
            ) {

                NormalTextComponent(value = UIText.StringResource(R.string.login).likeString(), context = context)
                HeadingTextComponent(value = UIText.StringResource(R.string.welcome_back).likeString(), context = context)
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = UIText.StringResource(R.string.email).likeString(),
                    painterResource(id = R.drawable.baseline_mail_outline_24),
                    onTextChanged = {
                        loginViewModel.onEvent(LoginEvent.EmailChanged(it), navController, context)
                    },
                    errorStatus = loginViewModel.loginState.value.emailError,
                    screen = Screen.LoginScreen,
                    context = context
                )

                if (!loginViewModel.errorEmailText.isNullOrEmpty() && !loginViewModel.errorEmailText.isNullOrBlank() && loginViewModel.errorEmailText != "") {
                    ErrorTextComponent(value = loginViewModel.errorEmailText)
                }

                PasswordTextFieldComponent(
                    labelValue = UIText.StringResource(R.string.password).likeString(),
                    painterResource(id = R.drawable.outline_lock_24),
                    onTextSelected = {
                        loginViewModel.onEvent(
                            LoginEvent.PasswordChanged(it),
                            navController,
                            context
                        )
                    },
                    errorStatus = loginViewModel.loginState.value.passwordError,
                    context = context
                )

                if (!loginViewModel.errorPasswordText.isNullOrEmpty() && !loginViewModel.errorPasswordText.isNullOrBlank() && loginViewModel.errorPasswordText != "") {
                    ErrorTextComponent(value = loginViewModel.errorPasswordText)
                }

                Spacer(modifier = Modifier.height(40.dp))
                ClickableUnderLinedTextComponent(
                    value = UIText.StringResource(R.string.i_forgot_my_password).likeString(),
                    onClick = {
                        navController.navigate(Screen.ForgotPasswordScreen.route)
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = UIText.StringResource(R.string.login).likeString(),
                    onButtonClicked = {
                        loginViewModel.onEvent(
                            LoginEvent.LoginButtonClicked,
                            navController,
                            context
                        )
                    },
                    isEnabled = loginViewModel.allValidationsPassed.value
                )

                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent(context = context)

                ClickableLoginTextComponent(
                    tryingToLogin = false,
                    onTextSelected = {
                        navController.navigate(Screen.RegisterScreen.route)
                    },
                    context = context
                )
            }
        }

        if (loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController(), context = MainActivity())
}