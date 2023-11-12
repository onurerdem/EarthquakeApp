package com.onurerdem.earthquakeapp.presentation.login

data class LoginState(
    var email  :String = "",
    var password  :String = "",

    var emailError :Boolean = false,
    var passwordError : Boolean = false
)