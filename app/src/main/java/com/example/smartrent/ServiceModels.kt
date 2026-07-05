package com.example.smartrent

// Dữ liệu gửi lên khi Thêm/Sửa dịch vụ
data class ServiceRequest(
    val name: String,
    val price: Double,
    val unit: String,
    val description: String?
)

// Cấu trúc một dịch vụ nhận về từ Database
data class ServiceData(
    val id: Int,
    val name: String,
    val price: Double,
    val unit: String,
    val description: String?,
    val created_at: String?,
    val updated_at: String?
)

// Response khi lấy danh sách dịch vụ (Trả về một Mảng/List)
data class ServiceListResponse(
    val success: Boolean,
    val message: String,
    val data: List<ServiceData>
)

// Response khi Thêm/Sửa/Xóa (Trả về một Object đơn lẻ hoặc null)
data class ServiceCrudResponse(
    val success: Boolean,
    val message: String,
    val data: ServiceData?
)

// Data class dùng để gói mảng ID gửi lên server
data class SyncServiceRequest(
    val service_ids: List<Int>
)
