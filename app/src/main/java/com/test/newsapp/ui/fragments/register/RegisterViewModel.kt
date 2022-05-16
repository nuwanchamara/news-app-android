package com.test.newsapp.ui.fragments.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.newsapp.models.User
import com.test.newsapp.services.UserService
import com.test.newsapp.usecases.ValidateEmail
import com.test.newsapp.usecases.ValidateName
import com.test.newsapp.usecases.ValidatePassword
import com.test.newsapp.usecases.ValidateRetypePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private var validateName: ValidateName = ValidateName(),
    private var validateEmail: ValidateEmail = ValidateEmail(),
    private var validatePassword: ValidatePassword = ValidatePassword(),
    private var validateRetypePassword: ValidateRetypePassword = ValidateRetypePassword(),
    private var userService: UserService = UserService.instance
) : ViewModel() {

    var state by mutableStateOf(RegistrationFormState())
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {

        when (event) {
            is RegistrationFormEvent.FirstNameChanged -> {
                state = state.copy(firstName = event.firstName)
            }
            is RegistrationFormEvent.LastNameChanged -> {
                state = state.copy(lastName = event.lastName)
            }
            is RegistrationFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.RetypePasswordPasswordChanged -> {
                state = state.copy(repeatedPassword = event.retypePassowrd)
            }
            is RegistrationFormEvent.AcceptTerms -> {
                state = state.copy(acceptedTerms = event.isAccepted)
            }
            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }

    }


    fun submitData() {

        val firstNameResult = validateName.execute(state.firstName)
        val lastNameResult = validateName.execute(state.lastName)
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val retypePasswordResult =
            validateRetypePassword.execute(state.password, state.repeatedPassword)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            passwordResult,
            retypePasswordResult
        ).any {
            !it.successful
        }


        if (hasError) {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Error)
            }
            return
        }
        val user = User(
            firstName = state.firstName,
            lastName = state.lastName,
            email = state.email,
            password = state.password
        )
        userService.register(user)
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }


    }


}

sealed class ValidationEvent {
    object Success : ValidationEvent()
    object Error : ValidationEvent()
}