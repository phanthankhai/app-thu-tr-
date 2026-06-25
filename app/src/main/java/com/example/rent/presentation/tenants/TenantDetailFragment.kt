package com.example.rent.presentation.tenants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.databinding.FragmentTenantDetailBinding

class TenantDetailFragment : Fragment() {
    private var _binding: FragmentTenantDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTenantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTenantButton.setOnClickListener {
            findNavController().navigate(R.id.action_tenant_detail_to_edit_tenant)
        }

        binding.viewRoomButton.setOnClickListener {
            findNavController().navigate(R.id.action_tenant_detail_to_room_detail)
        }

        binding.viewBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_tenant_detail_to_bills)
        }

        binding.createBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_tenant_detail_to_bills)
        }

        // TODO Phase 2E+: connect when transfer-room workflow is defined.
        binding.transferRoomButton.isEnabled = false

        // TODO Phase 2E+: connect when end-rent workflow is defined.
        binding.endRentButton.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
