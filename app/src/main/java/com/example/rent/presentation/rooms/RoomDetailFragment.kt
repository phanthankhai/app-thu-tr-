package com.example.rent.presentation.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.databinding.FragmentRoomDetailBinding

class RoomDetailFragment : Fragment() {
    private var _binding: FragmentRoomDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRoomDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editRoomButton.setOnClickListener {
            findNavController().navigate(R.id.action_room_detail_to_edit_room)
        }

        binding.viewBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_room_detail_to_bills)
        }

        binding.addTenantButton.setOnClickListener {
            findNavController().navigate(R.id.action_room_detail_to_tenants)
        }

        binding.createBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_room_detail_to_bills)
        }

        // TODO Phase 2D+: connect when a dedicated meter-reading route exists.
        binding.recordMeterButton.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
