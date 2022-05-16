package com.test.newsapp.ui.fragments.register

sealed class RegistrationFormEvent {
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class FirstNameChanged(val firstName: String) : RegistrationFormEvent()
    data class LastNameChanged(val lastName: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class RetypePasswordPasswordChanged(val retypePassowrd: String) : RegistrationFormEvent()
    data class AcceptTerms(val isAccepted: Boolean) : RegistrationFormEvent()
    object Submit : RegistrationFormEvent()

}