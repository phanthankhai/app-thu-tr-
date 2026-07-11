package com.example.smartrent

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

// 1. Dữ liệu gửi lên Backend (giống Body JSON trong Postman)
data class LoginRequest(
    val phone: String,
    val password: String
)

// 2. Dữ liệu Backend trả về (hứng Access Token)
data class AuthData(
    val access_token: String,
    val type: String,
    val expires_in: Int
)
data class User(
    val id: Int,
    val name: String,
    val phone: String,
    val role: String
)

data class LoginData(
    val user: User,
    val authorization: AuthData
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData
)

data class Room(
    val id: Int,
    val name: String,
    val price: Double,
    val area: Double,
    val status: String,
    val image: String?,
    val contract: ContractData?, // Thông tin hợp đồng hiện tại
    val tenants: List<TenantData>?, // Danh sách thành viên (dạng model cũ)
    val users: List<User>? // QUAN TRỌNG: Thêm dòng này vào cuối
)

data class RoomResponse(
    val success: Boolean,
    val data: List<Room>
)

data class RoomDetailResponse(
    val success: Boolean,
    val data: Room
)

data class BaseResponse(
    val success: Boolean,
    val message: String
)

data class AddTenantRequest(
    val room_id: Int,
    val name: String,
    val phone: String
)

data class PaymentResponse(
    val success: Boolean,
    val message: String,
    val data: List<PaymentData>
)

// Khung chứa thông tin 1 biên lai lẻ
data class PaymentData(
    val id: Int,
    val amount: String, // Trùng khớp với kiểu dữ liệu chuỗi chuỗi JSON thực tế
    val status: String,
    val payment_method: String,
    val user: PaymentUser?,
    val bill: PaymentBill?
)

// Thông tin người đóng
data class PaymentUser(
    val id: Int,
    val name: String,
    val phone: String?
)

// Thông tin hóa đơn & phòng
data class PaymentBill(
    val id: Int,
    val room: PaymentRoom?
)

data class PaymentRoom(
    val id: Int,
    val name: String
)



// 3. Khai báo các đường dẫn API
interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/logout")
    fun logout(@Header("Authorization") token: String): Call<BaseResponse>

    // Lấy danh sách phòng. Bắt buộc phải kẹp thêm Token vào Header để chứng minh thành viên
    @GET("admin/rooms")
    fun getRooms(@Header("Authorization") token: String): Call<RoomResponse>

    // LẤY CHI TIẾT 1 PHÒNG THEO ID
    @GET("admin/rooms/{id}")
    fun getRoomDetail(
        @Header("Authorization") token: String,
        @Path("id") roomId: Int
    ): Call<RoomDetailResponse>

    // HÀM MỚI: Thêm phòng mới kèm ảnh (Multipart)
    @Multipart
    @POST("admin/rooms")
    fun addRoom(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("area") area: RequestBody,
        @Part("status") status: RequestBody,
        @Part("description") description: RequestBody?,
        @Part image: MultipartBody.Part? // Khai báo phần gửi file ảnh (có thể null)
    ): Call<RoomDetailResponse> // Tái sử dụng RoomDetailResponse vì Laravel trả về thông tin phòng vừa tạo

    @POST("admin/bills")
    fun createBill(
        @Header("Authorization") token: String,
        @Body request: BillRequest
    ): Call<BillResponse>

    @GET("admin/bills")
    fun getBills(@Header("Authorization") token: String): Call<BillListResponse>

    @POST("admin/contracts")
    fun createContract(
        @Header("Authorization") token: String,
        @Body request: ContractRequest
    ): Call<ContractResponse>

    @GET("admin/services")
    fun getServices(@Header("Authorization") token: String): Call<ServiceListResponse>

    @POST("admin/services")
    fun createService(@Header("Authorization") token: String, @Body request: ServiceRequest): Call<ServiceCrudResponse>

    @PUT("admin/services/{id}")
    fun updateService(@Header("Authorization") token: String, @Path("id") id: Int, @Body request: ServiceRequest): Call<ServiceCrudResponse>

    @DELETE("admin/services/{id}")
    fun deleteService(@Header("Authorization") token: String, @Path("id") id: Int): Call<ServiceCrudResponse>

    // API gán dịch vụ cho phòng
    @POST("admin/rooms/{id}/services")
    fun syncRoomServices(
        @Header("Authorization") token: String,
        @Path("id") roomId: Int,
        @Body request: SyncServiceRequest
    ): Call<Any>

    // API Cập nhật trạng thái thanh toán hóa đơn
    @POST("admin/bills/{id}/pay")
    fun markBillAsPaid(
        @Header("Authorization") token: String,
        @Path("id") billId: Int
    ): Call<Any>

    // API Lấy danh sách khách thuê
    @GET("admin/tenants")
    fun getTenants(@Header("Authorization") token: String): Call<TenantListResponse>

    // API Chấm dứt hợp đồng
    @POST("admin/tenants/{id}/end-rent")
    fun endRent(
        @Header("Authorization") token: String,
        @Path("id") tenantId: Int
    ): Call<Any>

    @GET("admin/dashboard")
    fun getDashboard(@Header("Authorization") token: String): Call<DashboardResponse>

    @GET("tenant/my-dashboard")
    fun getMyDashboard(@Header("Authorization") token: String): Call<TenantDashboardResponse>

    @POST("tenant/bills/{id}/notify-payment")
    fun tenantNotifyPayment(
        @Header("Authorization") token: String,
        @Path("id") billId: Int,
        @Body body: Map<String, String>
    ): Call<BaseResponse>

    // Admin lấy toàn bộ sự cố
    @GET("admin/incidents")
    fun getAllIncidents(@Header("Authorization") token: String): Call<IncidentListResponse>

    // Admin cập nhật trạng thái sự cố
    @PUT("admin/incidents/{id}/status")
    fun updateIncidentStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body statusData: Map<String, String>
    ): Call<StatusResponse>

    // API Gửi báo cáo sự cố từ khách thuê
    @POST("tenant/report-incident") // Sửa đúng thành "tenant/report-incident"
    fun sendReport(
        @Header("Authorization") token: String,
        @Body request: Map<String, String>
    ): Call<Any>

    // Admin thêm thành viên vào phòng
    @POST("admin/rooms/add-tenant")
    fun addCoTenant(
        @Header("Authorization") token: String,
        @Body request: AddTenantRequest
    ): Call<BaseResponse>

    // Admin lấy danh sách biên lai lẻ đang chờ duyệt tiền
    @GET("admin/payments/pending")
    fun getPendingPayments(@Header("Authorization") token: String): Call<PaymentResponse>

    // Admin bấm nút "Xác nhận đã nhận tiền"
    @POST("admin/payments/{id}/approve")
    fun approvePayment(
        @Header("Authorization") token: String,
        @Path("id") paymentId: Int
    ): Call<BaseResponse>
}
