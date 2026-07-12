package com.example.smartrent

// Dữ liệu gửi đi khi tạo hợp đồng
data class ContractRequest(
    val room_id: Int,
    val tenant_name: String,
    val tenant_phone: String,
    val tenant_cccd: String,  // Thêm biến này
    val deposit: Double,
    val start_date: String,
    val end_date: String?     // Thêm biến này (Nullable)
)

// Dữ liệu nhận về từ API
data class ContractResponse(
    val success: Boolean,
    val message: String,
    val data: ContractData?
)

data class ContractData(
    val id: Int,
    val room_id: Int,
    val tenant_name: String,
    val tenant_phone: String,
    val tenant_cccd: String?,
    val deposit: Double,
    val start_date: String,
    val end_date: String?,
    val status: String,
    // ĐỔI TỪ BOOLEAN SANG INT Ở ĐÂY:
    val tenant_approved: Int,
    val admin_approved: Int
)