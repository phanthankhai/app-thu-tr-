package com.example.smartrent

// 1. Class tổng hứng dữ liệu API my-dashboard trả về
data class TenantDashboardResponse(
    val success: Boolean,
    val message: String?,
    val data: TenantDashboardData?
)

// 2. Class chứa 3 món Backend gửi
data class TenantDashboardData(
    val my_info: User,
    val room: Room,
    val bill: BillData? // Có thể null nếu tháng đó chưa tạo hóa đơn
)
