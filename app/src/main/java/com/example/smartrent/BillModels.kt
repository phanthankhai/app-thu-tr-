package com.example.smartrent

// Gói dữ liệu gửi lên
data class BillRequest(
    val room_id: Int,
    val billing_month: String,
    val old_electric: Int,
    val new_electric: Int,
    val old_water: Int,
    val new_water: Int
)

// Gói dữ liệu nhận về
data class BillResponse(
    val success: Boolean,
    val message: String,
    val data: BillInfoData?
)

data class BillInfoData(
    val bill_info: BillDetail?,
    val total_bill: Double,
    val split_details: Map<String, Any>? = null
)

data class BillDetail(
    val id: Int,
    val room_id: Int,
    val billing_month: String,
    val status: String
)

// Data class đại diện cho 1 hóa đơn (Dùng trong danh sách và dashboard)
data class BillData(
    val id: Int,
    val status: String,
    val old_electric: Int?,
    val new_electric: Int?,
    val old_water: Int?,
    val new_water: Int?,
    
    // BỔ SUNG LẠI CÁC BIẾN CHO ADMIN (BillAdapter không bị báo đỏ nữa)
    val room_id: Int?,
    val billing_month: String?,
    val total_amount: Double?,
    val room: Room?, // Để truy xuất được bill.room.name
    val my_payment_status: String? // Thêm dòng này để hứng trạng thái đóng tiền lẻ của chính mình
)

// Data class gói danh sách trả về
data class BillListResponse(
    val success: Boolean,
    val message: String,
    val data: List<BillData>
)
