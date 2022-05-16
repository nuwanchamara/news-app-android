package com.test.newsapp.ui.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
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
                }
            }
        }
    }


    private fun navigateToDashBoard() {

    }

    fun navigateToRegister() {

    }

    private fun setErrors() {
        binding.emailLabel.error = viewModel.state.emailError
        binding.passwordLabel.error = viewModel.state.passwordError
    }

}