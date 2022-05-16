package com.test.newsapp.ui.fragments.login

data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null

)