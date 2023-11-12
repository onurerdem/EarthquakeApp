package com.onurerdem.earthquakeapp.presentation.forgot_password

sealed class ForgotPasswordEvent {
    data class EmailChanged(val email:String): ForgotPasswordEvent()
    object ResetPasswordButtonClicked : ForgotPasswordEvent()
}