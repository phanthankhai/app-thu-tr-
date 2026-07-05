package com.example.smartrent

data class TenantDashboardResponse(
    val success: Boolean,
    val data: TenantDashboardData?
)

data class TenantDashboardData(
    val user: UserData,
    val room: TenantRoomData,
    val current_bill: TenantBillData?,
    val roommates: List<UserData>? // Thêm danh sách người ở chung
)

data class UserData(
    val name: String,
    val phone: String
)

data class TenantRoomData(
    val id: Int,
    val name: String,
    val price: String,
    val area: String,
    val status: String
)

data class TenantBillData(
    val id: Int,
    val total_amount: String,
    val status: String
)
