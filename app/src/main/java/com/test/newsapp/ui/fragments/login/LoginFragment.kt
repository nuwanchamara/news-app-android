package com.test.newsapp.ui.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.test.newsapp.R
import com.test.newsapp.databinding.FragmentLoginBinding
import com.test.newsapp.ui.fragments.register.ValidationEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.email.doAfterTextChanged {
            viewModel.onEvent(LoginFormEvent.EmailChanged(it.toString()))
        }
        binding.password.doAfterTextChanged {
            viewModel.onEvent(LoginFormEvent.PasswordChanged(it.toString()))
        }

        binding.submitBtn.setOnClickListener {
            viewModel.onEvent(LoginFormEvent.Submit)
        }
        binding.reginster.setOnClickListener {
            navigateToRegister()
        }
        receiveEvents()

    }

    fun receiveEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.validationEvents.collect { event: ValidationEvent ->
                when (event) {
                    is ValidationEvent.Success -> {
                        navigateToDashBoard()
                    }
                    is ValidationEvent.Error -> {
                        setErrors()
                    }
                    is ValidationEvent.LoginFailed -> {
                        Snackbar.make(requireView(), "Login Failed", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun navigateToDashBoard() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_loginFragment_to_dashboardActivity)
    }

    fun navigateToRegister() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_loginFragment_to_registerFragment2)
    }

    private fun setErrors() {
        binding.emailLabel.error = viewModel.state.emailError
        binding.emailLabel.isErrorEnabled = true
        binding.passwordLabel.error = viewModel.state.passwordError
    }

}