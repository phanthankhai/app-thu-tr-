package com.example.rent.presentation.tenants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.common.UiState
import com.example.rent.core.mock.SmartRentMockData
import com.example.rent.databinding.FragmentTenantsBinding
import com.example.rent.presentation.model.TenantUiModel

class TenantsFragment : Fragment() {
    private var _binding: FragmentTenantsBinding? = null
    private val binding get() = _binding!!
    private val tenantsState: UiState<List<TenantUiModel>> = UiState.Success(SmartRentMockData.tenants)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTenantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTenantButton.setOnClickListener {
            findNavController().navigate(R.id.action_tenants_to_add_tenant)
        }

        binding.emptyAddTenantButton.setOnClickListener {
            findNavController().navigate(R.id.action_tenants_to_add_tenant)
        }

        binding.tenantMinhAnhCard.setOnClickListener {
            findNavController().navigate(R.id.action_tenants_to_tenant_detail)
        }

        binding.viewTenantMinhAnhButton.setOnClickListener {
            findNavController().navigate(R.id.action_tenants_to_tenant_detail)
        }

        binding.tenantLanHuongCard.setOnClickListener {
            findNavController().navigate(R.id.action_tenants_to_tenant_detail)
        }

        binding.tenantQuocBaoCard.setOnClickListener {
            findNavController().navigate(R.id.action_tenants_to_tenant_detail)
        }

        renderState(tenantsState)
    }

    private fun renderState(state: UiState<List<TenantUiModel>>) {
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
        setTenantCardsVisible(false)
        binding.tenantsEmptyStateCard.visibility = View.GONE
    }

    private fun renderContent(tenants: List<TenantUiModel>) {
        val hasTenants = tenants.isNotEmpty()
        setTenantCardsVisible(hasTenants)
        binding.tenantsEmptyStateCard.visibility = if (hasTenants) View.GONE else View.VISIBLE
    }

    private fun renderEmpty() {
        setTenantCardsVisible(false)
        binding.tenantsEmptyStateCard.visibility = View.VISIBLE
    }

    private fun renderError(message: String) {
        setTenantCardsVisible(false)
        binding.tenantsEmptyStateCard.visibility = View.GONE
    }

    private fun setTenantCardsVisible(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.tenantMinhAnhCard.visibility = visibility
        binding.tenantLanHuongCard.visibility = visibility
        binding.tenantQuocBaoCard.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
