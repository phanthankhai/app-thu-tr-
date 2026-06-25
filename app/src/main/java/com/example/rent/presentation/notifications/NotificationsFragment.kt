package com.example.rent.presentation.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.common.UiState
import com.example.rent.core.mock.SmartRentMockData
import com.example.rent.databinding.FragmentNotificationsBinding
import com.example.rent.presentation.model.NotificationUiModel

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val notificationsState: UiState<List<NotificationUiModel>> =
        UiState.Success(SmartRentMockData.notifications)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val openDetail = View.OnClickListener {
            findNavController().navigate(R.id.action_notifications_to_notification_detail)
        }
        binding.billNotificationCard.setOnClickListener(openDetail)
        binding.viewBillNotificationButton.setOnClickListener(openDetail)
        binding.roomNotificationCard.setOnClickListener(openDetail)
        binding.tenantNotificationCard.setOnClickListener(openDetail)
        binding.meterNotificationCard.setOnClickListener(openDetail)

        // Mark-all-read is a placeholder until notification state exists.
        binding.markAllReadButton.isEnabled = false

        renderState(notificationsState)
    }

    private fun renderState(state: UiState<List<NotificationUiModel>>) {
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
        setNotificationCardsVisible(false)
    }

    private fun renderContent(notifications: List<NotificationUiModel>) {
        setNotificationCardsVisible(notifications.isNotEmpty())
        // Keep disabled until a real notification state store exists.
        binding.markAllReadButton.isEnabled = false
    }

    private fun renderEmpty() {
        setNotificationCardsVisible(false)
        binding.markAllReadButton.isEnabled = false
    }

    private fun renderError(message: String) {
        setNotificationCardsVisible(false)
        binding.markAllReadButton.isEnabled = false
    }

    private fun setNotificationCardsVisible(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.billNotificationCard.visibility = visibility
        binding.roomNotificationCard.visibility = visibility
        binding.tenantNotificationCard.visibility = visibility
        binding.meterNotificationCard.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
