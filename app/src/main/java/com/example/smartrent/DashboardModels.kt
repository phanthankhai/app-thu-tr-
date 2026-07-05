package com.example.smartrent

data class DashboardResponse(
    val success: Boolean,
    val data: DashboardData
)

data class DashboardData(
    val empty_rooms: Int,
    val total_tenants: Int,
    val current_month_revenue: Double,
    val unpaid_bills_count: Int
)
