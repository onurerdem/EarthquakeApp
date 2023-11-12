package com.onurerdem.earthquakeapp.presentation.register

sealed class RegisterEvent{

    data class FirstNameChanged(val firstName:String) : RegisterEvent()
    data class LastNameChanged(val lastName:String) : RegisterEvent()
    data class EmailChanged(val email:String): RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()

    data class PrivacyPolicyCheckBoxClicked(val status:Boolean) : RegisterEvent()

    object RegisterButtonClicked : RegisterEvent()
}