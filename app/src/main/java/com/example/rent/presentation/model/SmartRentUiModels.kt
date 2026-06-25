package com.example.rent.presentation.model

data class HomeOverviewUiModel(
    val label: String,
    val value: String,
    val hint: String,
)

data class HomeQuickActionUiModel(
    val id: String,
    val label: String,
    val enabled: Boolean = true,
)

data class RecentActivityUiModel(
    val title: String,
    val body: String,
    val timeLabel: String,
)

enum class RoomStatus {
    OCCUPIED,
    AVAILABLE,
    MAINTENANCE,
}

data class RoomUiModel(
    val id: String,
    val name: String,
    val meta: String,
    val priceDisplay: String,
    val note: String,
    val status: RoomStatus,
)

enum class TenantStatus {
    ACTIVE,
    ENDING_SOON,
    DEBT,
    LEFT,
}

data class TenantUiModel(
    val id: String,
    val name: String,
    val phoneDisplay: String,
    val roomLabel: String,
    val note: String,
    val status: TenantStatus,
)

enum class BillStatus {
    PENDING,
    PAID,
    OVERDUE,
    DRAFT,
}

data class BillUiModel(
    val id: String,
    val title: String,
    val meta: String,
    val amountDisplay: String,
    val dueLabel: String,
    val status: BillStatus,
)

data class MeterReadingUiModel(
    val roomName: String,
    val periodLabel: String,
    val electricStart: String,
    val waterStart: String,
)

data class PaymentUiModel(
    val billLabel: String,
    val totalDisplay: String,
    val remainingDisplay: String,
)

data class ProfileSummaryUiModel(
    val label: String,
    val value: String,
)

data class ProfileUiModel(
    val displayName: String,
    val roleLabel: String,
    val contactDisplay: String,
    val summaries: List<ProfileSummaryUiModel>,
)

enum class NotificationType {
    BILL,
    ROOM,
    TENANT,
    METER,
    SYSTEM,
}

data class NotificationUiModel(
    val id: String,
    val title: String,
    val body: String,
    val timeLabel: String,
    val type: NotificationType,
    val isUnread: Boolean,
)
