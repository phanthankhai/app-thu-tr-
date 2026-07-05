package com.example.smartrent

// Data class đại diện cho 1 khách thuê
data class TenantData(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String?,
    val room_id: Int?,
    val room: RoomData? // Dùng chung RoomData đã tạo ở phần trước
)

// Data class hứng danh sách trả về
data class TenantListResponse(
    val success: Boolean,
    val message: String,
    val data: List<TenantData>
)
