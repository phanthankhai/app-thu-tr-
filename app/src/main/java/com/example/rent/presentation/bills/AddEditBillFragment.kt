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
import com.example.rent.databinding.FragmentAddEditBillBinding

class AddEditBillFragment : Fragment() {
    private var _binding: FragmentAddEditBillBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditBillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getString(ARG_FORM_MODE) == FORM_MODE_EDIT) {
            binding.formTitleTextView.setText(R.string.bill_edit_title)
        }

        UiFeedback.clearErrorOnTextChange(binding.billRoomInputLayout, binding.billRoomEditText)
        UiFeedback.clearErrorOnTextChange(binding.billPeriodInputLayout, binding.billPeriodEditText)
        UiFeedback.clearErrorOnTextChange(binding.billRentAmountInputLayout, binding.billRentAmountEditText)
        UiFeedback.clearErrorOnTextChange(
            binding.billElectricAmountInputLayout,
            binding.billElectricAmountEditText,
        )
        UiFeedback.clearErrorOnTextChange(binding.billWaterAmountInputLayout, binding.billWaterAmountEditText)
        UiFeedback.clearErrorOnTextChange(binding.billFeeInputLayout, binding.billFeeEditText)
        UiFeedback.clearErrorOnTextChange(binding.billDiscountInputLayout, binding.billDiscountEditText)
        UiFeedback.clearErrorOnTextChange(binding.billDueDateInputLayout, binding.billDueDateEditText)

        binding.saveBillButton.setOnClickListener {
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
        val room = binding.billRoomEditText.text?.toString().orEmpty().trim()
        val period = binding.billPeriodEditText.text?.toString().orEmpty().trim()
        val rentAmount = binding.billRentAmountEditText.text?.toString().orEmpty().trim()
        val electricAmount = binding.billElectricAmountEditText.text?.toString().orEmpty().trim()
        val waterAmount = binding.billWaterAmountEditText.text?.toString().orEmpty().trim()
        val feeAmount = binding.billFeeEditText.text?.toString().orEmpty().trim()
        val discountAmount = binding.billDiscountEditText.text?.toString().orEmpty().trim()
        val dueDate = binding.billDueDateEditText.text?.toString().orEmpty().trim()
        var isValid = true

        binding.billRoomInputLayout.error = null
        binding.billPeriodInputLayout.error = null
        binding.billRentAmountInputLayout.error = null
        binding.billElectricAmountInputLayout.error = null
        binding.billWaterAmountInputLayout.error = null
        binding.billFeeInputLayout.error = null
        binding.billDiscountInputLayout.error = null
        binding.billDueDateInputLayout.error = null

        if (!FormValidators.isRequired(room)) {
            binding.billRoomInputLayout.error = getString(R.string.bill_error_room_required)
            isValid = false
        }

        if (!FormValidators.isRequired(period)) {
            binding.billPeriodInputLayout.error = getString(R.string.bill_error_period_required)
            isValid = false
        }

        if (!FormValidators.isRequired(rentAmount)) {
            binding.billRentAmountInputLayout.error = getString(R.string.bill_error_amount_required)
            isValid = false
        } else if (!FormValidators.isValidPositiveAmount(rentAmount)) {
            binding.billRentAmountInputLayout.error = getString(R.string.error_invalid_amount)
            isValid = false
        }

        if (electricAmount.isNotBlank() && !FormValidators.isValidNonNegativeNumber(electricAmount)) {
            binding.billElectricAmountInputLayout.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (waterAmount.isNotBlank() && !FormValidators.isValidNonNegativeNumber(waterAmount)) {
            binding.billWaterAmountInputLayout.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (feeAmount.isNotBlank() && !FormValidators.isValidNonNegativeNumber(feeAmount)) {
            binding.billFeeInputLayout.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (discountAmount.isNotBlank() && !FormValidators.isValidNonNegativeNumber(discountAmount)) {
            binding.billDiscountInputLayout.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (!FormValidators.isRequired(dueDate)) {
            binding.billDueDateInputLayout.error = getString(R.string.error_required_field)
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val ARG_FORM_MODE = "formMode"
        const val FORM_MODE_EDIT = "edit"
        const val MOCK_NAV_DELAY_MS = 350L
    }
}
