package com.example.rent.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.common.UiState
import com.example.rent.core.mock.SmartRentMockData
import com.example.rent.databinding.FragmentHomeBinding
import com.example.rent.presentation.model.HomeOverviewUiModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeState: UiState<List<HomeOverviewUiModel>> =
        UiState.Success(SmartRentMockData.homeOverview)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_notifications)
        }

        binding.addRoomButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_rooms)
        }

        binding.addTenantButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_tenants)
        }

        binding.createBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_bills)
        }

        binding.openBillsReminderButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_bills)
        }

        // TODO Phase 2C+: connect this action when a dedicated meter-reading route exists.
        binding.recordMeterButton.isEnabled = false

        renderState(homeState)
    }

    private fun renderState(state: UiState<List<HomeOverviewUiModel>>) {
        when (state) {
            UiState.Loading -> renderLoading()
            is UiState.Success -> {
                if (state.data.isEmpty()) renderEmpty() else renderContent(state.data)
            }
            is UiState.Empty -> renderEmpty()
            is UiState.Error -> renderError(state.message)
        }
    }

    private fun renderLoading() {
        binding.root.visibility = View.VISIBLE
    }

    private fun renderContent(overview: List<HomeOverviewUiModel>) {
        binding.root.visibility = View.VISIBLE
        binding.recordMeterButton.isEnabled =
            SmartRentMockData.homeQuickActions.firstOrNull { it.id == "record_meter" }?.enabled == true
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
