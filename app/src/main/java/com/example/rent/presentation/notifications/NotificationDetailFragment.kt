package com.example.rent.presentation.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.databinding.FragmentNotificationDetailBinding

class NotificationDetailFragment : Fragment() {
    private var _binding: FragmentNotificationDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewRelatedBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_notification_detail_to_bills)
        }
        binding.viewRelatedRoomButton.setOnClickListener {
            findNavController().navigate(R.id.action_notification_detail_to_rooms)
        }

        // Mark-read requires real notification state and is intentionally disabled in Phase 2F.
        binding.markReadButton.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
