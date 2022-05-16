package com.test.newsapp.ui.fragments.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.test.newsapp.databinding.FragmentRegisterBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private var _binding: FragmentRegisterBinding? = null
    private lateinit var viewModel: RegisterViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java).apply {
        }
        receiveEvents()
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstName.doAfterTextChanged {
            viewModel.onEvent(RegistrationFormEvent.FirstNameChanged(it.toString()))
        }

        binding.lastName.doAfterTextChanged {
            viewModel.onEvent(RegistrationFormEvent.LastNameChanged(it.toString()))
        }

        binding.email.doAfterTextChanged {
            viewModel.onEvent(RegistrationFormEvent.EmailChanged(it.toString()))
        }

        binding.password.doAfterTextChanged {
            viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it.toString()))
        }

        binding.retypePassword.doAfterTextChanged {
            viewModel.onEvent(RegistrationFormEvent.RetypePasswordPasswordChanged(it.toString()))
        }


        binding.submitBtn.setOnClickListener {
            viewModel.onEvent(RegistrationFormEvent.Submit)
        }

    }

    private fun receiveEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.validationEvents.collect { event: ValidationEvent ->
                when (event) {
                    is ValidationEvent.Success -> {
                        // goto Login
                        navigateToLogin()
                    }
                    is ValidationEvent.Error -> {
                        setErrors()
                    }
                }
            }
        }

    }

    private fun navigateToLogin() {

    }

    private fun setErrors() {
        binding.firstNameLabel.error = viewModel.state.firstNameError
        binding.lastNameLabel.error = viewModel.state.LastNameError
        binding.emailLabel.error = viewModel.state.emailError
        binding.passwordLabel.error = viewModel.state.passwordError
        binding.retypePasswordLabel.error = viewModel.state.repeatedPassword
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}