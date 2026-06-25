package com.example.rent.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.ui.UiFeedback
import com.example.rent.core.utils.FormValidators
import com.example.rent.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        binding.forgotPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot_password)
        }

        UiFeedback.clearErrorOnTextChange(binding.contactInputLayout, binding.contactEditText)
        UiFeedback.clearErrorOnTextChange(binding.passwordInputLayout, binding.passwordEditText)

        binding.loginButton.setOnClickListener {
            if (validateForm()) {
                UiFeedback.hideKeyboard(binding.root)
                UiFeedback.showShortMessage(binding.root, getString(R.string.message_mock_login_success))
                findNavController().navigate(R.id.action_login_to_home)
            }
        }
    }

    private fun validateForm(): Boolean {
        val contact = binding.contactEditText.text?.toString().orEmpty().trim()
        val password = binding.passwordEditText.text?.toString().orEmpty()
        var isValid = true

        binding.contactInputLayout.error = null
        binding.passwordInputLayout.error = null

        if (!FormValidators.isRequired(contact)) {
            binding.contactInputLayout.error = getString(R.string.error_required_field)
            isValid = false
        } else if (!FormValidators.isValidEmailOrPhone(contact)) {
            binding.contactInputLayout.error = getString(R.string.error_invalid_contact)
            isValid = false
        }

        if (!FormValidators.isRequired(password)) {
            binding.passwordInputLayout.error = getString(R.string.auth_error_password_required)
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
