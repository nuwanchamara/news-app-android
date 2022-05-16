package com.test.newsapp.ui.fragments.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.newsapp.services.UserService
import com.test.newsapp.ui.fragments.register.ValidationEvent
import com.test.newsapp.usecases.ValidateEmail
import com.test.newsapp.usecases.ValidatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private var validateEmail: ValidateEmail = ValidateEmail(),
    private var validatePassword: ValidatePassword = ValidatePassword(),
    val userService: UserService = UserService.instance
) : ViewModel() {

    var state by mutableStateOf(LoginFormState())
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is LoginFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is LoginFormEvent.Submit -> {
                submitData()
            }
        }

    }


    private fun submitData() {

        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any {
            !it.successful
        }

        if (hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage ?: "",
                passwordError = passwordResult.errorMessage
            )

            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Error)
            }
            return
        }
        val isLoginSuccess = userService.login(username = state.email, password = state.password)
        if (!isLoginSuccess) {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.LoginFailed)
            }

            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

}