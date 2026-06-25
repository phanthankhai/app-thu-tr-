package com.example.rent.presentation.bills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.ui.UiFeedback
import com.example.rent.core.utils.FormValidators
import com.example.rent.databinding.FragmentPaymentRecordBinding

class PaymentRecordFragment : Fragment() {
    private var _binding: FragmentPaymentRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPaymentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UiFeedback.clearErrorOnTextChange(binding.paymentAmountInputLayout, binding.paymentAmountEditText)
        UiFeedback.clearErrorOnTextChange(binding.paymentMethodInputLayout, binding.paymentMethodEditText)
        UiFeedback.clearErrorOnTextChange(binding.paymentDateInputLayout, binding.paymentDateEditText)

        binding.savePaymentButton.setOnClickListener {
            if (validateForm()) {
                UiFeedback.hideKeyboard(binding.root)
                UiFeedback.showShortMessage(binding.root, getString(R.string.message_mock_save_success))
                binding.root.postDelayed({ findNavController().popBackStack() }, MOCK_NAV_DELAY_MS)
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validateForm(): Boolean {
        val amountText = binding.paymentAmountEditText.text?.toString().orEmpty().trim()
        val paymentMethod = binding.paymentMethodEditText.text?.toString().orEmpty().trim()
        val paymentDate = binding.paymentDateEditText.text?.toString().orEmpty().trim()
        var isValid = true

        binding.paymentAmountInputLayout.error = null
        binding.paymentMethodInputLayout.error = null
        binding.paymentDateInputLayout.error = null

        if (!FormValidators.isRequired(amountText)) {
            binding.paymentAmountInputLayout.error = getString(R.string.payment_error_amount_required)
            isValid = false
        } else if (!FormValidators.isValidPositiveAmount(amountText)) {
            binding.paymentAmountInputLayout.error = getString(R.string.error_invalid_amount)
            isValid = false
        }

        if (!FormValidators.isRequired(paymentMethod)) {
            binding.paymentMethodInputLayout.error = getString(R.string.error_required_field)
            isValid = false
        }

        if (!FormValidators.isRequired(paymentDate)) {
            binding.paymentDateInputLayout.error = getString(R.string.error_required_field)
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val MOCK_NAV_DELAY_MS = 350L
    }
}
