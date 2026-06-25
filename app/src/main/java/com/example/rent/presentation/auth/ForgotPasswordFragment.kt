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
import com.example.rent.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToLoginButton.setOnClickListener {
            navigateBackToLogin()
        }

        UiFeedback.clearErrorOnTextChange(binding.contactInputLayout, binding.contactEditText)

        binding.sendResetButton.setOnClickListener {
            if (validateForm()) {
                UiFeedback.hideKeyboard(binding.root)
                UiFeedback.showShortMessage(binding.root, getString(R.string.message_mock_reset_sent))
            }
        }
    }

    private fun navigateBackToLogin() {
        val navController = findNavController()
        if (!navController.popBackStack(R.id.loginFragment, false)) {
            navController.navigate(R.id.action_forgot_password_to_login)
        }
    }

    private fun validateForm(): Boolean {
        val contact = binding.contactEditText.text?.toString().orEmpty().trim()
        binding.contactInputLayout.error = null

        return if (!FormValidators.isRequired(contact)) {
            binding.contactInputLayout.error = getString(R.string.error_required_field)
            false
        } else if (!FormValidators.isValidEmailOrPhone(contact)) {
            binding.contactInputLayout.error = getString(R.string.error_invalid_contact)
            false
        } else {
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
