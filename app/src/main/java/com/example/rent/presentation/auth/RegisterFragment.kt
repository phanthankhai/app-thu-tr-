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
import com.example.rent.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToLoginButton.setOnClickListener {
            navigateBackToLogin()
        }

        UiFeedback.clearErrorOnTextChange(binding.fullNameInputLayout, binding.fullNameEditText)
        UiFeedback.clearErrorOnTextChange(binding.contactInputLayout, binding.contactEditText)
        UiFeedback.clearErrorOnTextChange(binding.passwordInputLayout, binding.passwordEditText)
        UiFeedback.clearErrorOnTextChange(
            binding.confirmPasswordInputLayout,
            binding.confirmPasswordEditText,
        )

        binding.registerButton.setOnClickListener {
            if (validateForm()) {
                UiFeedback.hideKeyboard(binding.root)
                UiFeedback.showShortMessage(binding.root, getString(R.string.message_mock_save_success))
                navigateBackToLogin()
            }
        }
    }

    private fun navigateBackToLogin() {
        val navController = findNavController()
        if (!navController.popBackStack(R.id.loginFragment, false)) {
            navController.navigate(R.id.action_register_to_login)
        }
    }

    private fun validateForm(): Boolean {
        val fullName = binding.fullNameEditText.text?.toString().orEmpty().trim()
        val contact = binding.contactEditText.text?.toString().orEmpty().trim()
        val password = binding.passwordEditText.text?.toString().orEmpty()
        val confirmPassword = binding.confirmPasswordEditText.text?.toString().orEmpty()
        var isValid = true

        binding.fullNameInputLayout.error = null
        binding.contactInputLayout.error = null
        binding.passwordInputLayout.error = null
        binding.confirmPasswordInputLayout.error = null

        if (!FormValidators.isRequired(fullName)) {
            binding.fullNameInputLayout.error = getString(R.string.auth_error_full_name_required)
            isValid = false
        }

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
        } else if (!FormValidators.isValidPassword(password)) {
            binding.passwordInputLayout.error = getString(R.string.error_password_min_length)
            isValid = false
        }

        if (!FormValidators.isRequired(confirmPassword)) {
            binding.confirmPasswordInputLayout.error =
                getString(R.string.auth_error_confirm_password_required)
            isValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordInputLayout.error =
                getString(R.string.error_password_mismatch)
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
