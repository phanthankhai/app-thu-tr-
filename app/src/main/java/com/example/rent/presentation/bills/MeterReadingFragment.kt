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
import com.example.rent.databinding.FragmentMeterReadingBinding

class MeterReadingFragment : Fragment() {
    private var _binding: FragmentMeterReadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMeterReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UiFeedback.clearErrorOnTextChange(binding.electricStartInputLayout, binding.electricStartEditText)
        UiFeedback.clearErrorOnTextChange(binding.electricEndInputLayout, binding.electricEndEditText)
        UiFeedback.clearErrorOnTextChange(binding.waterStartInputLayout, binding.waterStartEditText)
        UiFeedback.clearErrorOnTextChange(binding.waterEndInputLayout, binding.waterEndEditText)

        listOf(
            binding.electricStartEditText,
            binding.electricEndEditText,
            binding.waterStartEditText,
            binding.waterEndEditText,
        ).forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) updateUsagePreview()
            }
        }

        binding.saveMeterButton.setOnClickListener {
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
        val electricStart = binding.electricStartEditText.text?.toString().orEmpty().trim()
        val electricEnd = binding.electricEndEditText.text?.toString().orEmpty().trim()
        val waterStart = binding.waterStartEditText.text?.toString().orEmpty().trim()
        val waterEnd = binding.waterEndEditText.text?.toString().orEmpty().trim()
        var isValid = true

        binding.electricStartInputLayout.error = null
        binding.electricEndInputLayout.error = null
        binding.waterStartInputLayout.error = null
        binding.waterEndInputLayout.error = null

        if (!FormValidators.isRequired(electricStart)) {
            binding.electricStartInputLayout.error = getString(R.string.error_meter_start_required)
            isValid = false
        } else if (!FormValidators.isValidNumber(electricStart)) {
            binding.electricStartInputLayout.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (!FormValidators.isRequired(electricEnd)) {
            binding.electricEndInputLayout.error = getString(R.string.meter_error_electric_end_required)
            isValid = false
        } else if (!FormValidators.isValidNumber(electricEnd)) {
            binding.electricEndInputLayout.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (!FormValidators.isRequired(waterStart)) {
            binding.waterStartInputLayout.error = getString(R.string.error_meter_start_required)
            isValid = false
        } else if (!FormValidators.isValidNumber(waterStart)) {
            binding.waterStartInputLayout.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (!FormValidators.isRequired(waterEnd)) {
            binding.waterEndInputLayout.error = getString(R.string.meter_error_water_end_required)
            isValid = false
        } else if (!FormValidators.isValidNumber(waterEnd)) {
            binding.waterEndInputLayout.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (isValid && !FormValidators.isEndReadingValid(electricStart, electricEnd)) {
            binding.electricEndInputLayout.error = getString(R.string.error_meter_reading_invalid)
            isValid = false
        }

        if (isValid && !FormValidators.isEndReadingValid(waterStart, waterEnd)) {
            binding.waterEndInputLayout.error = getString(R.string.error_meter_reading_invalid)
            isValid = false
        }

        if (isValid) updateUsagePreview()

        return isValid
    }

    private fun updateUsagePreview() {
        val electricStart = FormValidators.parseNumber(
            binding.electricStartEditText.text?.toString().orEmpty(),
        )
        val electricEnd = FormValidators.parseNumber(
            binding.electricEndEditText.text?.toString().orEmpty(),
        )
        val waterStart = FormValidators.parseNumber(
            binding.waterStartEditText.text?.toString().orEmpty(),
        )
        val waterEnd = FormValidators.parseNumber(
            binding.waterEndEditText.text?.toString().orEmpty(),
        )

        if (electricStart == null || electricEnd == null || waterStart == null || waterEnd == null) {
            binding.usagePreviewTextView.setText(R.string.meter_usage_preview_placeholder)
            return
        }

        val electricUsage = electricEnd - electricStart
        val waterUsage = waterEnd - waterStart
        if (electricUsage < 0.0 || waterUsage < 0.0) {
            binding.usagePreviewTextView.setText(R.string.meter_usage_preview_placeholder)
            return
        }

        binding.usagePreviewTextView.text = getString(
            R.string.meter_usage_preview_value,
            formatReading(electricUsage),
            formatReading(waterUsage),
        )
    }

    private fun formatReading(value: Double): String =
        if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val MOCK_NAV_DELAY_MS = 350L
    }
}
