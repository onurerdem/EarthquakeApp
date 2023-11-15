package com.onurerdem.earthquakeapp.presentation.forgot_password.views

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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.onurerdem.earthquakeapp.presentation.ErrorTextComponent
import com.onurerdem.earthquakeapp.presentation.HeadingTextComponent
import com.onurerdem.earthquakeapp.presentation.MainActivity
import com.onurerdem.earthquakeapp.presentation.MyTextFieldComponent
import com.onurerdem.earthquakeapp.presentation.NormalTextComponent
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.forgot_password.ForgotPasswordEvent
import com.onurerdem.earthquakeapp.presentation.forgot_password.ForgotPasswordViewModel
import com.onurerdem.earthquakeapp.presentation.isDarkThemeMode
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    forgotPasswordViewModel: ForgotPasswordViewModel = viewModel(),
    navController: NavController,
    context: MainActivity
) {
    val scope = rememberCoroutineScope()

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

                NormalTextComponent(value = "Merhaba", context = context)
                HeadingTextComponent(value = "Şifrenizi yenileyebilirsiniz.", context = context)
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(labelValue = "Email",
                    painterResource(id = R.drawable.baseline_mail_outline_24),
                    onTextChanged = {
                        scope.launch {
                            forgotPasswordViewModel.onEvent(ForgotPasswordEvent.EmailChanged(it), navController, context)
                        }
                    },
                    errorStatus = forgotPasswordViewModel.forgotPasswordState.value.emailError,
                    screen = Screen.ForgotPasswordScreen,
                    context = context
                )

                if (!forgotPasswordViewModel.errorEmailText.isNullOrEmpty() && !forgotPasswordViewModel.errorEmailText.isNullOrBlank() && forgotPasswordViewModel.errorEmailText != "") {
                    ErrorTextComponent(value = forgotPasswordViewModel.errorEmailText)
                }

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = "Şifreyi yenile",
                    onButtonClicked = {
                        scope.launch {
                            forgotPasswordViewModel.onEvent(ForgotPasswordEvent.ResetPasswordButtonClicked, navController, context)
                        }
                    },
                    isEnabled = forgotPasswordViewModel.allValidationsPassed.value
                )
            }
        }

        if(forgotPasswordViewModel.forgotPasswordInProgress.value) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfForgotPasswordScreen() {
    ForgotPasswordScreen(navController = rememberNavController(), context = MainActivity())
}