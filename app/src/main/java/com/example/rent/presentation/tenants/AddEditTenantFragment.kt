package com.example.rent.presentation.tenants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.ui.UiFeedback
import com.example.rent.core.utils.FormValidators
import com.example.rent.databinding.FragmentAddEditTenantBinding

class AddEditTenantFragment : Fragment() {
    private var _binding: FragmentAddEditTenantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditTenantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getString(ARG_FORM_MODE) == FORM_MODE_EDIT) {
            binding.formTitleTextView.setText(R.string.tenant_edit_title)
        }

        UiFeedback.clearErrorOnTextChange(binding.tenantNameInputLayout, binding.tenantNameEditText)
        UiFeedback.clearErrorOnTextChange(binding.tenantPhoneInputLayout, binding.tenantPhoneEditText)
        UiFeedback.clearErrorOnTextChange(binding.tenantEmailInputLayout, binding.tenantEmailEditText)
        UiFeedback.clearErrorOnTextChange(binding.tenantRoomInputLayout, binding.tenantRoomEditText)

        binding.saveTenantButton.setOnClickListener {
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
        val tenantName = binding.tenantNameEditText.text?.toString().orEmpty().trim()
        val tenantPhone = binding.tenantPhoneEditText.text?.toString().orEmpty().trim()
        val tenantEmail = binding.tenantEmailEditText.text?.toString().orEmpty().trim()
        val tenantRoom = binding.tenantRoomEditText.text?.toString().orEmpty().trim()
        var isValid = true

        binding.tenantNameInputLayout.error = null
        binding.tenantPhoneInputLayout.error = null
        binding.tenantEmailInputLayout.error = null
        binding.tenantRoomInputLayout.error = null

        if (!FormValidators.isRequired(tenantName)) {
            binding.tenantNameInputLayout.error = getString(R.string.tenant_error_name_required)
            isValid = false
        }

        if (!FormValidators.isRequired(tenantPhone)) {
            binding.tenantPhoneInputLayout.error = getString(R.string.tenant_error_phone_required)
            isValid = false
        } else if (!FormValidators.isValidPhone(tenantPhone)) {
            binding.tenantPhoneInputLayout.error = getString(R.string.error_invalid_phone)
            isValid = false
        }

        if (tenantEmail.isNotBlank() && !FormValidators.isValidEmail(tenantEmail)) {
            binding.tenantEmailInputLayout.error = getString(R.string.error_invalid_email)
            isValid = false
        }

        if (!FormValidators.isRequired(tenantRoom)) {
            binding.tenantRoomInputLayout.error = getString(R.string.tenant_error_room_required)
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
