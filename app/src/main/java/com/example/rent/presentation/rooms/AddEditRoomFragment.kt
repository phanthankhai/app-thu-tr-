package com.example.rent.presentation.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.ui.UiFeedback
import com.example.rent.core.utils.FormValidators
import com.example.rent.databinding.FragmentAddEditRoomBinding

class AddEditRoomFragment : Fragment() {
    private var _binding: FragmentAddEditRoomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddEditRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getString(ARG_FORM_MODE) == FORM_MODE_EDIT) {
            binding.formTitleTextView.setText(R.string.room_edit_title)
        }

        UiFeedback.clearErrorOnTextChange(binding.roomNameInputLayout, binding.roomNameEditText)
        UiFeedback.clearErrorOnTextChange(binding.roomPriceInputLayout, binding.roomPriceEditText)
        UiFeedback.clearErrorOnTextChange(binding.roomCapacityInputLayout, binding.roomCapacityEditText)
        UiFeedback.clearErrorOnTextChange(binding.roomAreaInputLayout, binding.roomAreaEditText)

        binding.saveRoomButton.setOnClickListener {
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
        val roomName = binding.roomNameEditText.text?.toString().orEmpty().trim()
        val roomPrice = binding.roomPriceEditText.text?.toString().orEmpty().trim()
        val roomCapacity = binding.roomCapacityEditText.text?.toString().orEmpty().trim()
        val roomArea = binding.roomAreaEditText.text?.toString().orEmpty().trim()
        var isValid = true

        binding.roomNameInputLayout.error = null
        binding.roomPriceInputLayout.error = null
        binding.roomCapacityInputLayout.error = null
        binding.roomAreaInputLayout.error = null

        if (!FormValidators.isRequired(roomName)) {
            binding.roomNameInputLayout.error = getString(R.string.room_error_name_required)
            isValid = false
        }

        if (!FormValidators.isRequired(roomPrice)) {
            binding.roomPriceInputLayout.error = getString(R.string.room_error_price_required)
            isValid = false
        } else if (!FormValidators.isValidPositiveAmount(roomPrice)) {
            binding.roomPriceInputLayout.error = getString(R.string.error_required_positive_number)
            isValid = false
        }

        if (!FormValidators.isRequired(roomCapacity)) {
            binding.roomCapacityInputLayout.error = getString(R.string.error_required_field)
            isValid = false
        } else if (!FormValidators.isValidPositiveAmount(roomCapacity)) {
            binding.roomCapacityInputLayout.error = getString(R.string.error_required_positive_number)
            isValid = false
        }

        if (roomArea.isNotBlank() && !FormValidators.isValidPositiveAmount(roomArea)) {
            binding.roomAreaInputLayout.error = getString(R.string.error_invalid_number)
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
