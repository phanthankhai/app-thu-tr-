package com.example.rent.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.common.UiState
import com.example.rent.core.mock.SmartRentMockData
import com.example.rent.databinding.FragmentProfileBinding
import com.example.rent.presentation.model.ProfileUiModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileState: UiState<ProfileUiModel> = UiState.Success(SmartRentMockData.profile)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsMenuItem.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        }
        binding.notificationsMenuItem.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_notifications)
        }

        // Phase 2F only prepares static UI. Profile edit/help/policy/logout stay placeholders.
        binding.editProfileButton.isEnabled = false
        binding.logoutButton.isEnabled = false

        renderState(profileState)
    }

    private fun renderState(state: UiState<ProfileUiModel>) {
        when (state) {
            UiState.Loading -> renderLoading()
            is UiState.Success -> renderContent(state.data)
            is UiState.Empty -> renderEmpty()
            is UiState.Error -> renderError(state.message)
        }
    }

    private fun renderLoading() {
        binding.root.visibility = View.VISIBLE
    }

    private fun renderContent(profile: ProfileUiModel) {
        binding.root.visibility = View.VISIBLE
    }

    private fun renderEmpty() {
        binding.root.visibility = View.VISIBLE
    }

    private fun renderError(message: String) {
        binding.root.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
