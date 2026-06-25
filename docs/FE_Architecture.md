# FRONTEND ARCHITECTURE DOCUMENT v2.0

# DỰ ÁN SMARTRENT / RENTHUB MOBILE APP

> **Version:** 2.0 | **Cập nhật:** 2025 | **Platform:** Android Native Kotlin

---

## MỤC LỤC

1. [Mục tiêu kiến trúc](#1-mục-tiêu-của-kiến-trúc-frontend)
2. [Định hướng công nghệ](#2-định-hướng-công-nghệ)
3. [Mô hình kiến trúc tổng thể](#3-mô-hình-kiến-trúc-tổng-thể)
4. [Nguyên tắc thiết kế bắt buộc](#4-nguyên-tắc-thiết-kế-bắt-buộc)
5. [Cấu trúc thư mục](#5-cấu-trúc-thư-mục-tổng-thể)
6. [Thư mục `core`](#6-thư-mục-core)
7. [Thư mục `domain`](#7-thư-mục-domain)
8. [Thư mục `data`](#8-thư-mục-data)
9. [Thư mục `presentation`](#9-thư-mục-presentation)
10. [Thư mục `navigation`](#10-thư-mục-navigation)
11. [Thư mục `feature`](#11-thư-mục-feature)
12. [Kiến trúc từng feature](#12-kiến-trúc-từng-feature)
13. [Chuẩn ViewModel, UiState, Event, Effect](#13-chuẩn-viewmodel-uistate-event-effect)
14. [Chuẩn xử lý trạng thái màn hình](#14-chuẩn-xử-lý-trạng-thái-màn-hình)
15. [Error Handling Pattern](#15-error-handling-pattern)
16. [Dependency Injection (Hilt)](#16-dependency-injection-hilt)
17. [Testing Strategy](#17-testing-strategy)
18. [Performance Optimization](#18-performance-optimization)
19. [Accessibility](#19-accessibility)
20. [DeepLink Handling](#20-deeplink-handling)
21. [Quy tắc phân quyền frontend](#21-quy-tắc-phân-quyền-frontend)
22. [API Mapping theo module](#22-api-mapping-theo-module)
23. [Upload ảnh](#23-upload-ảnh)
24. [Cache local](#24-cache-local)
25. [Realtime và Notification](#25-realtime-và-notification)
26. [Danh sách màn hình đầy đủ](#26-danh-sách-màn-hình-đầy-đủ)
27. [Thứ tự triển khai Frontend](#27-thứ-tự-triển-khai-frontend)
28. [Phạm vi MVP Frontend](#28-phạm-vi-mvp-frontend)
29. [Tiêu chuẩn hoàn thành màn hình & feature](#29-tiêu-chuẩn-hoàn-thành-màn-hình--feature)
30. [Kết luận](#30-kết-luận-kiến-trúc)

---

## 1. Mục tiêu của kiến trúc Frontend

Frontend của SmartRent / RentHub là ứng dụng di động Android dùng cho hai nhóm người dùng chính:

* **Chủ trọ / Owner** – quản lý toàn bộ dãy trọ
* **Người thuê / Tenant** – xem hóa đơn, giao tiếp và báo lỗi

### Nghiệp vụ chính

| Nhóm | Nghiệp vụ |
|------|-----------|
| Chung | Đăng nhập, quản lý phiên, chọn dãy trọ, thông báo, chat, hồ sơ cá nhân |
| Chủ trọ | Quản lý phòng, quản lý người thuê, chốt điện nước, phát hành hóa đơn, xác nhận thanh toán, báo cáo doanh thu |
| Người thuê | Xem hóa đơn, thanh toán, bảng tin nội khu, ticket báo lỗi |

### Mục tiêu kỹ thuật

* Dễ mở rộng khi thêm module mới
* Dễ chia việc cho nhiều thành viên
* Dễ nối Backend API
* Dễ test từng màn hình
* Dễ bảo trì
* **Tách biệt rõ ràng** giữa UI, logic nghiệp vụ và xử lý dữ liệu
* Phù hợp với Android Native Kotlin + Jetpack Compose
* **Hỗ trợ Offline-first** cho các tính năng cốt lõi

---

## 2. Định hướng công nghệ

### Stack công nghệ đề xuất

| Thành phần | Công nghệ | Phiên bản khuyến nghị |
|---|---|---|
| Ngôn ngữ | Kotlin | 1.9+ |
| UI | Jetpack Compose | 1.6+ |
| Điều hướng | Navigation Compose | 2.7+ |
| State management | ViewModel + StateFlow | AndroidX |
| Bất đồng bộ | Kotlin Coroutines + Flow | 1.7+ |
| Gọi API | Retrofit + OkHttp | 2.9+ / 4.12+ |
| Serialization | Kotlin Serialization hoặc Gson | — |
| Lưu token/session | DataStore Preferences | 1.1+ |
| Cache local | Room Database | 2.6+ |
| Load ảnh | Coil | 2.5+ |
| Realtime | Firebase Realtime DB / WebSocket (OkHttp) | — |
| Push Notification | Firebase Cloud Messaging (FCM) | — |
| Dependency Injection | Hilt | 2.50+ |
| Analytics | Firebase Analytics (tùy chọn) | — |
| Crash reporting | Firebase Crashlytics (tùy chọn) | — |

> **Lưu ý:** Nếu dự án đang dùng XML layout, thay phần `Screen` bằng `Activity / Fragment / Adapter / XML layout`. Kiến trúc còn lại giữ nguyên.

---

## 3. Mô hình kiến trúc tổng thể

Frontend sử dụng mô hình:

```
MVVM + Clean Architecture + Feature-first
```

### Luồng dữ liệu một chiều (Unidirectional Data Flow)

```
┌─────────────────────────────────────────────┐
│               UI Layer                       │
│  Screen ──→ Event/Intent ──→ ViewModel       │
│    ↑                              │          │
│    └──────── UiState/Effect ──────┘          │
└─────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────┐
│              Domain Layer                    │
│           UseCase (Business Logic)           │
│           Repository Interface               │
│           Domain Model                       │
└─────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────┐
│               Data Layer                     │
│  Repository Implementation                   │
│  Remote API (Retrofit DTO)                   │
│  Local DB (Room Entity)                      │
│  Mapper (DTO/Entity → Domain)                │
└─────────────────────────────────────────────┘
```

### Ý nghĩa từng tầng

| Tầng | Vai trò | Phụ thuộc |
|------|---------|-----------|
| **Presentation** | Hiển thị giao diện, nhận thao tác người dùng | Domain |
| **ViewModel** | Quản lý state màn hình, gọi use case, xử lý side effect | Domain |
| **Domain** | Model nghiệp vụ, use case, repository interface | Không phụ thuộc tầng khác |
| **Data** | Gọi API, đọc/ghi local DB, map DTO sang domain model | Domain |
| **Core** | Thành phần dùng chung toàn app | Không phụ thuộc feature |

---

## 4. Nguyên tắc thiết kế bắt buộc

### 4.1. Không để Activity xử lý quá nhiều logic

`MainActivity` chỉ làm:
* Khởi tạo theme
* Khởi tạo navigation host
* Gắn root composable
* Xử lý permission runtime (có thể delegate xuống PermissionHandler)

### 4.2. Mỗi màn hình phải có ViewModel riêng

```
LoginScreen.kt
LoginViewModel.kt
LoginUiState.kt
LoginEvent.kt
LoginEffect.kt    ← thêm mới so với v1
```

### 4.3. Không gọi API trực tiếp trong UI

❌ Sai:
```kotlin
// Trong Screen/Composable
val response = retrofit.create(AuthApi::class.java).login(...)
```

✅ Đúng:
```
Screen → onEvent(LoginEvent.Submit)
  → ViewModel.handleEvent()
    → LoginUseCase.invoke()
      → AuthRepository.login()
        → AuthApi.login()
```

### 4.4. Không hard-code dữ liệu nghiệp vụ

Mọi trạng thái, vai trò, loại bài viết phải là `enum class` hoặc `sealed class` trong `core/constants`.

### 4.5. Mọi dữ liệu nghiệp vụ phải gắn với `currentSpaceId`

Hệ thống là Multi-Tenancy. App luôn lưu:

```kotlin
data class AppSession(
    val currentUserId: String,
    val currentSpaceId: String,
    val currentRoleInSpace: UserRole,
    val currentRoomId: String?,
    val accessToken: String,
    val refreshToken: String
)
```

### 4.6. ⚡ Mới — Không share ViewModel giữa các màn hình không liên quan

* ViewModel nên được scoped đúng với lifecycle của màn hình
* Chỉ dùng `sharedViewModel` khi hai màn hình cùng một nested nav graph và thực sự cần chia sẻ state

### 4.7. ⚡ Mới — Luôn dùng `sealed interface` cho UiState phức tạp

Với màn hình có nhiều trạng thái phân biệt, dùng:

```kotlin
sealed interface InvoiceListUiState {
    data object Loading : InvoiceListUiState
    data class Success(val invoices: List<Invoice>) : InvoiceListUiState
    data object Empty : InvoiceListUiState
    data class Error(val message: UiText) : InvoiceListUiState
}
```

### 4.8. ⚡ Mới — Effect chỉ được consume một lần

Dùng `Channel` thay vì `StateFlow` cho Effect (side effects như toast, navigate):

```kotlin
private val _effect = Channel<LoginEffect>()
val effect = _effect.receiveAsFlow()
```

---

## 5. Cấu trúc thư mục tổng thể

```
com.smartrent.renthub/
│
├── SmartRentApplication.kt
├── MainActivity.kt
│
├── core/
│   ├── common/
│   ├── constants/
│   ├── network/
│   ├── storage/
│   ├── notification/
│   ├── realtime/
│   ├── permission/
│   ├── result/
│   ├── security/
│   ├── di/              ← ⚡ Mới — Hilt modules
│   └── utils/
│
├── data/
│   ├── remote/
│   ├── local/
│   ├── mapper/
│   └── repository/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── navigation/
├── presentation/
└── feature/
```

---

## 6. Thư mục `core`

### 6.1. `core/common`

```
common/
├── BaseViewModel.kt
├── UiText.kt           ← String resource abstraction
├── AppConfig.kt
└── AppDispatchers.kt
```

**`UiText.kt`** — Chuẩn hóa thông điệp lỗi:

```kotlin
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
    }
}
```

**`BaseViewModel.kt`**:

```kotlin
abstract class BaseViewModel : ViewModel() {
    protected fun launchWithLoading(
        onError: (UiText) -> Unit,
        block: suspend () -> Unit
    ) = viewModelScope.launch {
        try {
            block()
        } catch (e: NetworkException) {
            onError(e.toUiText())
        } catch (e: Exception) {
            onError(UiText.DynamicString(e.message ?: "Unknown error"))
        }
    }
}
```

### 6.2. `core/constants`

```
constants/
├── ApiConstants.kt
├── AppConstants.kt
├── RoleConstants.kt
├── InvoiceStatus.kt
├── TicketStatus.kt
├── PostType.kt
├── PaymentStatus.kt
└── RoomStatus.kt       ← ⚡ Mới
```

**Định nghĩa đầy đủ:**

```kotlin
enum class UserRole { OWNER, TENANT }

enum class InvoiceStatus {
    DRAFT, PROCESSING, PUBLISHED,
    PARTIALLY_PAID, PAID, OVERDUE, CANCELLED
}

enum class TicketStatus { OPEN, IN_PROGRESS, RESOLVED, CANCELLED }

enum class TicketPriority { LOW, MEDIUM, HIGH }   // ⚡ Mới

enum class PostType { ANNOUNCEMENT, MARKETPLACE, DISCUSSION, DEVICE_REPORT }

enum class PaymentStatus { PENDING, CONFIRMED, REJECTED }

enum class RoomStatus { EMPTY, OCCUPIED, MAINTENANCE, INACTIVE }

enum class ChatRoomType {                           // ⚡ Mới
    DIRECT_OWNER_TENANT, ROOM_GROUP, SPACE_GROUP
}

enum class NotificationType {                      // ⚡ Mới
    NEW_INVOICE, PAYMENT_CONFIRMED, PAYMENT_REJECTED,
    NEW_MESSAGE, NEW_ANNOUNCEMENT, TICKET_UPDATED,
    TENANT_ADDED, ROOM_CHANGED
}
```

### 6.3. `core/network`

```
network/
├── ApiClient.kt
├── AuthInterceptor.kt
├── TokenAuthenticator.kt
├── NetworkMonitor.kt
├── ApiResponse.kt
├── ErrorResponse.kt
├── NetworkException.kt
└── SafeApiCall.kt      ← ⚡ Mới
```

| File | Chức năng |
|------|-----------|
| `ApiClient.kt` | Khởi tạo Retrofit, OkHttp |
| `AuthInterceptor.kt` | Gắn access token vào header |
| `TokenAuthenticator.kt` | Refresh token khi 401 |
| `NetworkMonitor.kt` | Theo dõi trạng thái mạng (Flow) |
| `ApiResponse.kt` | Chuẩn hóa response chung |
| `ErrorResponse.kt` | Parse lỗi từ backend |
| `NetworkException.kt` | Sealed class lỗi mạng |
| `SafeApiCall.kt` | Extension function bọc try/catch |

**`SafeApiCall.kt`** — ⚡ Mới:

```kotlin
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): Result<T> = withContext(dispatcher) {
    try {
        Result.Success(apiCall())
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        val errorResponse = parseError(errorBody)
        Result.Error(NetworkException.HttpError(e.code(), errorResponse?.message))
    } catch (e: IOException) {
        Result.Error(NetworkException.NetworkError)
    } catch (e: Exception) {
        Result.Error(NetworkException.UnknownError(e.message))
    }
}
```

**`NetworkException.kt`**:

```kotlin
sealed class NetworkException : Exception() {
    data class HttpError(val code: Int, val msg: String?) : NetworkException()
    data object NetworkError : NetworkException()           // Mất mạng
    data object TimeoutError : NetworkException()           // Timeout
    data class UnknownError(val msg: String?) : NetworkException()

    fun toUiText(): UiText = when (this) {
        is HttpError -> when (code) {
            401 -> UiText.StringResource(R.string.error_unauthorized)
            403 -> UiText.StringResource(R.string.error_forbidden)
            404 -> UiText.StringResource(R.string.error_not_found)
            500 -> UiText.StringResource(R.string.error_server)
            else -> UiText.DynamicString(msg ?: "HTTP $code")
        }
        NetworkError -> UiText.StringResource(R.string.error_no_internet)
        TimeoutError -> UiText.StringResource(R.string.error_timeout)
        is UnknownError -> UiText.DynamicString(msg ?: "Unknown error")
    }
}
```

### 6.4. `core/storage`

```
storage/
├── TokenManager.kt
├── UserSessionManager.kt
├── SpaceSessionManager.kt
└── DataStoreKeys.kt
```

**`TokenManager.kt`** — dùng DataStore với encryption:

```kotlin
interface TokenManager {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    suspend fun saveTokens(access: String, refresh: String)
    suspend fun clearTokens()
}
```

### 6.5. `core/result`

```
result/
└── Result.kt
```

**`Result.kt`** — ⚡ Thêm chi tiết:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: NetworkException) : Result<Nothing>()
    data object Loading : Result<Nothing>()

    val isSuccess get() = this is Success
    val isError get() = this is Error

    fun getOrNull(): T? = (this as? Success)?.data
    fun exceptionOrNull(): NetworkException? = (this as? Error)?.exception
}
```

### 6.6. `core/utils`

```
utils/
├── DateTimeUtils.kt
├── CurrencyUtils.kt
├── FileUtils.kt
├── ImageUtils.kt
├── ValidationUtils.kt   ← ⚡ Mới
└── ExtensionFunctions.kt ← ⚡ Mới
```

**`ValidationUtils.kt`** — ⚡ Mới:

```kotlin
object ValidationUtils {
    fun isValidPhone(phone: String): Boolean =
        phone.matches(Regex("^0[0-9]{9}$"))

    fun isValidPassword(password: String): Boolean =
        password.length >= 8

    fun isValidAmount(amount: String): Boolean =
        amount.toLongOrNull()?.let { it > 0 } ?: false
}
```

### 6.7. `core/di` ← ⚡ Mới

```
di/
├── NetworkModule.kt
├── DatabaseModule.kt
├── RepositoryModule.kt
├── UseCaseModule.kt
└── StorageModule.kt
```

Xem chi tiết tại [Section 16 — Dependency Injection](#16-dependency-injection-hilt).

---

## 7. Thư mục `domain`

### 7.1. `domain/model`

```
model/
├── User.kt
├── UserRole.kt
├── AppSession.kt        ← ⚡ Mới
├── RentalSpace.kt
├── Room.kt
├── Tenant.kt
├── RoomMember.kt
├── MeterReading.kt
├── BillingRule.kt
├── ShareRatio.kt
├── Invoice.kt
├── InvoiceDetail.kt
├── Payment.kt
├── CommunityPost.kt
├── Comment.kt
├── Ticket.kt
├── ChatRoom.kt
├── Message.kt
├── Notification.kt
├── Report.kt
└── PagedResult.kt       ← ⚡ Mới
```

### 7.2. Các domain model quan trọng

#### User

```kotlin
data class User(
    val id: String,
    val fullName: String,
    val phone: String,
    val avatarUrl: String?,
    val firstLoginRequired: Boolean,
    val createdAt: String
)
```

#### RentalSpace

```kotlin
data class RentalSpace(
    val id: String,
    val name: String,
    val address: String,
    val currentUserRole: UserRole,
    val totalRooms: Int,
    val occupiedRooms: Int
)
```

#### Room

```kotlin
data class Room(
    val id: String,
    val spaceId: String,
    val name: String,
    val floor: Int?,
    val price: Long,
    val maxMembers: Int,
    val currentMembers: Int,
    val status: RoomStatus
)
```

#### Invoice

```kotlin
data class Invoice(
    val id: String,
    val roomId: String,
    val roomName: String,
    val month: String,           // "yyyy-MM"
    val totalAmount: Long,
    val paidAmount: Long,
    val remainingAmount: Long,   // ⚡ Mới — tính sẵn
    val status: InvoiceStatus,
    val dueDate: String,
    val details: List<InvoiceDetail>
)
```

#### ShareRatio

```kotlin
data class ShareRatio(
    val tenantId: String,
    val tenantName: String,
    val ratio: Double,           // 0.0 – 1.0
    val note: String?
)
```

#### Ticket

```kotlin
data class Ticket(
    val id: String,
    val title: String,
    val description: String,
    val roomName: String,
    val reporterName: String,
    val status: TicketStatus,
    val priority: TicketPriority, // ⚡ Mới
    val imageUrls: List<String>,
    val createdAt: String,
    val updatedAt: String
)
```

#### PagedResult ← ⚡ Mới

```kotlin
data class PagedResult<T>(
    val data: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalCount: Int,
    val hasNextPage: Boolean
)
```

### 7.3. `domain/repository`

```
repository/
├── AuthRepository.kt
├── SpaceRepository.kt
├── RoomRepository.kt
├── TenantRepository.kt
├── BillingRepository.kt
├── InvoiceRepository.kt
├── PaymentRepository.kt
├── CommunityRepository.kt
├── TicketRepository.kt
├── ChatRepository.kt
├── NotificationRepository.kt
├── ReportRepository.kt
└── FileRepository.kt
```

Repository interface **chỉ khai báo contract**, không chứa implementation:

```kotlin
interface InvoiceRepository {
    suspend fun getInvoiceList(
        spaceId: String,
        month: String?,
        roomId: String?,
        status: InvoiceStatus?,
        page: Int
    ): Result<PagedResult<Invoice>>

    suspend fun getInvoiceDetail(invoiceId: String): Result<Invoice>

    fun observeInvoices(spaceId: String): Flow<List<Invoice>>  // ⚡ Mới — observe từ Room
}
```

### 7.4. `domain/usecase`

```
usecase/
├── auth/
│   ├── LoginUseCase.kt
│   ├── LogoutUseCase.kt
│   ├── RefreshTokenUseCase.kt
│   ├── ChangeFirstPasswordUseCase.kt
│   ├── GetCurrentUserUseCase.kt
│   └── CheckAuthStateUseCase.kt
│
├── space/
│   ├── GetSpaceListUseCase.kt
│   ├── SelectSpaceUseCase.kt
│   └── CreateSpaceUseCase.kt
│
├── room/
│   ├── GetRoomListUseCase.kt
│   ├── GetRoomDetailUseCase.kt
│   ├── CreateRoomUseCase.kt
│   └── UpdateRoomUseCase.kt
│
├── tenant/
│   ├── GetTenantListUseCase.kt
│   ├── AddTenantUseCase.kt
│   ├── AssignTenantToRoomUseCase.kt
│   └── RemoveTenantFromRoomUseCase.kt
│
├── billing/
│   ├── GetBillingConfigUseCase.kt
│   ├── UpdateBillingConfigUseCase.kt
│   ├── CreateMeterReadingUseCase.kt
│   ├── GetMeterReadingUseCase.kt
│   ├── CalculateInvoicePreviewUseCase.kt
│   ├── PublishInvoiceUseCase.kt
│   └── GetBillingJobStatusUseCase.kt
│
├── invoice/
│   ├── GetInvoiceListUseCase.kt
│   ├── GetInvoiceDetailUseCase.kt
│   └── GetMyCurrentInvoiceUseCase.kt
│
├── payment/
│   ├── UploadPaymentProofUseCase.kt
│   ├── ConfirmPaymentUseCase.kt
│   └── RejectPaymentUseCase.kt
│
├── community/
│   ├── GetPostListUseCase.kt
│   ├── GetPostDetailUseCase.kt
│   ├── CreatePostUseCase.kt
│   └── AddCommentUseCase.kt
│
├── ticket/
│   ├── CreateTicketUseCase.kt
│   ├── GetTicketListUseCase.kt
│   ├── GetTicketDetailUseCase.kt
│   └── UpdateTicketStatusUseCase.kt
│
├── chat/
│   ├── GetChatRoomListUseCase.kt
│   ├── GetMessageListUseCase.kt
│   └── SendMessageUseCase.kt
│
├── notification/
│   ├── GetNotificationListUseCase.kt
│   └── MarkNotificationReadUseCase.kt
│
└── report/
    └── GetReportSummaryUseCase.kt
```

**UseCase mẫu:**

```kotlin
class LoginUseCase(
    private val authRepository: AuthRepository,
    private val sessionManager: UserSessionManager
) {
    suspend operator fun invoke(phone: String, password: String): Result<User> {
        if (!ValidationUtils.isValidPhone(phone))
            return Result.Error(NetworkException.UnknownError("Số điện thoại không hợp lệ"))

        return when (val result = authRepository.login(phone, password)) {
            is Result.Success -> {
                sessionManager.saveUser(result.data)
                result
            }
            is Result.Error -> result
            else -> result
        }
    }
}
```

---

## 8. Thư mục `data`

### 8.1. `data/remote/api`

```
api/
├── AuthApi.kt
├── SpaceApi.kt
├── RoomApi.kt
├── TenantApi.kt
├── BillingApi.kt
├── InvoiceApi.kt
├── PaymentApi.kt
├── CommunityApi.kt
├── TicketApi.kt
├── ChatApi.kt
├── NotificationApi.kt
├── ReportApi.kt
└── FileApi.kt
```

**Ví dụ `InvoiceApi.kt`:**

```kotlin
interface InvoiceApi {
    @GET("spaces/{spaceId}/invoices")
    suspend fun getInvoiceList(
        @Path("spaceId") spaceId: String,
        @Query("month") month: String?,
        @Query("roomId") roomId: String?,
        @Query("status") status: String?,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ApiResponse<PagedDto<InvoiceDto>>

    @GET("invoices/{invoiceId}")
    suspend fun getInvoiceDetail(
        @Path("invoiceId") invoiceId: String
    ): ApiResponse<InvoiceDto>
}
```

### 8.2. `data/remote/dto`

```
dto/
├── auth/
│   ├── LoginResponseDto.kt
│   └── UserDto.kt
├── space/
├── room/
├── tenant/
├── billing/
├── invoice/
│   ├── InvoiceDto.kt
│   ├── InvoiceDetailDto.kt
│   └── PagedDto.kt          ← ⚡ Mới (generic wrapper)
├── payment/
├── community/
├── ticket/
├── chat/
├── notification/
└── report/
```

**DTO không được dùng trực tiếp trong UI.** Mọi DTO phải qua mapper trước khi đến ViewModel.

### 8.3. `data/remote/request`

```
request/
├── auth/
│   ├── LoginRequest.kt
│   └── ChangePasswordRequest.kt
├── room/
│   └── CreateRoomRequest.kt
├── tenant/
│   └── AddTenantRequest.kt
├── billing/
│   ├── CreateMeterReadingRequest.kt
│   └── PublishInvoiceRequest.kt
├── community/
│   └── CreatePostRequest.kt
├── ticket/
│   └── CreateTicketRequest.kt
└── chat/
    └── SendMessageRequest.kt
```

### 8.4. `data/local`

```
local/
├── database/
│   └── AppDatabase.kt
├── dao/
│   ├── UserDao.kt
│   ├── RentalSpaceDao.kt
│   ├── RoomDao.kt
│   ├── InvoiceDao.kt
│   ├── CommunityPostDao.kt
│   ├── MessageDao.kt
│   └── NotificationDao.kt
└── entity/
    ├── UserEntity.kt
    ├── RentalSpaceEntity.kt
    ├── RoomEntity.kt
    ├── InvoiceEntity.kt
    ├── CommunityPostEntity.kt
    ├── MessageEntity.kt
    └── NotificationEntity.kt
```

| Dữ liệu | Cache | Lý do |
|---------|-------|-------|
| User | ✅ | Giữ phiên đăng nhập |
| RentalSpace | ✅ | Chọn nhanh dãy trọ |
| Room | ✅ | Load danh sách phòng nhanh |
| Invoice | ✅ | Người thuê xem lại khi mạng yếu |
| CommunityPost | ✅ | Bảng tin lướt mượt |
| Message | ✅ | Xem lại tin nhắn cũ |
| Notification | ✅ | Xem lại thông báo |

### 8.5. `data/mapper`

```
mapper/
├── UserMapper.kt
├── RentalSpaceMapper.kt
├── RoomMapper.kt
├── InvoiceMapper.kt
├── TicketMapper.kt
├── MessageMapper.kt
└── NotificationMapper.kt
```

**Pattern chuẩn:**

```kotlin
object InvoiceMapper {
    fun InvoiceDto.toDomain(): Invoice = Invoice(
        id = id,
        roomId = roomId,
        roomName = roomName,
        month = month,
        totalAmount = totalAmount,
        paidAmount = paidAmount,
        remainingAmount = totalAmount - paidAmount,
        status = InvoiceStatus.valueOf(status),
        dueDate = dueDate,
        details = details.map { it.toDomain() }
    )

    fun InvoiceEntity.toDomain(): Invoice = Invoice(/* ... */)

    fun Invoice.toEntity(): InvoiceEntity = InvoiceEntity(/* ... */)
}
```

### 8.6. `data/repository`

Repository implementation áp dụng **offline-first** pattern:

```kotlin
class InvoiceRepositoryImpl(
    private val api: InvoiceApi,
    private val dao: InvoiceDao,
    private val mapper: InvoiceMapper
) : InvoiceRepository {

    override suspend fun getInvoiceList(
        spaceId: String, month: String?, roomId: String?,
        status: InvoiceStatus?, page: Int
    ): Result<PagedResult<Invoice>> = safeApiCall {
        val response = api.getInvoiceList(spaceId, month, roomId, status?.name, page)
        val invoices = response.data.items.map { it.toDomain() }
        // Cache page 1 locally
        if (page == 1) dao.replaceAll(invoices.map { it.toEntity() })
        PagedResult(invoices, page, response.data.totalPages, response.data.totalCount, response.data.hasNext)
    }

    override fun observeInvoices(spaceId: String): Flow<List<Invoice>> =
        dao.observeBySpace(spaceId).map { entities -> entities.map { it.toDomain() } }
}
```

---

## 9. Thư mục `presentation`

### 9.1. Theme

```
theme/
├── Color.kt
├── Type.kt
├── Shape.kt
├── Spacing.kt          ← ⚡ Mới — spacing scale
├── Elevation.kt        ← ⚡ Mới
└── SmartRentTheme.kt
```

**Gợi ý design tokens:**

| Token | Giá trị |
|-------|---------|
| Màu chính | `#1E88E5` (Blue 600) |
| Màu phụ | `#43A047` (Green 600) |
| Cảnh báo | `#F57C00` (Orange 700) |
| Lỗi | `#E53935` (Red 600) |
| Surface | `#FFFFFF` |
| Background | `#F5F5F5` |
| Font chính | Roboto / Inter |
| Corner radius Card | `12dp` |
| Corner radius Button | `8dp` |

**`Spacing.kt`:**

```kotlin
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
}
```

### 9.2. Component dùng chung

```
component/
├── button/
│   ├── PrimaryButton.kt
│   ├── SecondaryButton.kt
│   ├── DangerButton.kt
│   ├── OutlinedButton.kt    ← ⚡ Mới
│   └── LoadingButton.kt
│
├── input/
│   ├── AppTextField.kt
│   ├── PhoneTextField.kt
│   ├── PasswordTextField.kt
│   ├── MoneyTextField.kt
│   ├── SearchTextField.kt
│   └── MultilineTextField.kt ← ⚡ Mới
│
├── card/
│   ├── AppCard.kt
│   ├── SummaryCard.kt
│   ├── InfoCard.kt
│   └── ClickableCard.kt     ← ⚡ Mới
│
├── dialog/
│   ├── ConfirmDialog.kt
│   ├── ErrorDialog.kt
│   ├── LoadingDialog.kt
│   ├── DatePickerDialog.kt
│   └── BottomSheetDialog.kt ← ⚡ Mới
│
├── empty/
│   ├── EmptyView.kt
│   └── ErrorView.kt
│
├── loading/
│   ├── FullScreenLoading.kt
│   ├── ShimmerCard.kt
│   └── PullRefreshLoading.kt
│
├── topbar/
│   ├── AppTopBar.kt
│   ├── BackTopBar.kt
│   └── SearchTopBar.kt
│
├── badge/
│   ├── RoleBadge.kt
│   ├── StatusBadge.kt
│   ├── InvoiceStatusBadge.kt
│   └── TicketStatusBadge.kt
│
└── paging/                   ← ⚡ Mới
    ├── PagingFooter.kt
    └── EndOfListView.kt
```

**Ví dụ `LoadingButton.kt`:**

```kotlin
@Composable
fun LoadingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(text)
        }
    }
}
```

---

## 10. Thư mục `navigation`

```
navigation/
├── AppNavGraph.kt
├── AuthNavGraph.kt
├── OwnerNavGraph.kt
├── TenantNavGraph.kt
├── MainBottomBar.kt
├── Screen.kt
├── NavigationArgs.kt
└── DeepLinkHandler.kt    ← ⚡ Mới
```

### 10.1. Định nghĩa route type-safe

```kotlin
sealed class Screen(val route: String) {
    // Auth
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object FirstLoginPassword : Screen("first_login_password")
    data object SpaceSelection : Screen("space_selection")

    // Owner
    data object OwnerHome : Screen("owner_home")
    data object RoomList : Screen("room_list")
    data class RoomDetail(val roomId: String = "{roomId}") :
        Screen("room_detail/{roomId}") {
        fun createRoute(id: String) = "room_detail/$id"
    }

    // ... các route khác
}
```

### 10.2. Luồng điều hướng tổng thể

```
Splash
│
├── [Token invalid / không có token]
│   └── → Login
│       └── [firstLoginRequired = true] → FirstLoginPassword
│
├── [Token hợp lệ, chưa chọn space]
│   └── → SpaceSelection
│       └── [Chọn xong] → Home (theo role)
│
├── [Token hợp lệ, role = OWNER]
│   └── → OwnerNavGraph (Bottom Navigation)
│
└── [Token hợp lệ, role = TENANT]
    └── → TenantNavGraph (Bottom Navigation)
```

### 10.3. Bottom Navigation cho Chủ trọ

| Tab | Icon | Destination |
|-----|------|-------------|
| Tổng quan | Home | OwnerHome |
| Phòng | Apartment | RoomList |
| Hóa đơn | Receipt | InvoiceList |
| Cộng đồng | Forum | CommunityFeed |
| Tài khoản | Person | Profile |

### 10.4. Bottom Navigation cho Người thuê

| Tab | Icon | Destination |
|-----|------|-------------|
| Trang chủ | Home | TenantHome |
| Hóa đơn | Receipt | MyInvoice |
| Bảng tin | Forum | CommunityFeed |
| Chat | Chat | ChatRoomList |
| Tài khoản | Person | Profile |

---

## 11. Thư mục `feature`

Mỗi feature là một module nghiệp vụ độc lập, tự chứa mọi thứ cần thiết:

```
feature/
├── splash/
├── auth/
├── onboarding/
├── space/
├── owner_home/
├── tenant_home/
├── room/
├── tenant/
├── billing/
├── invoice/
├── payment/
├── community/
├── ticket/
├── chat/
├── notification/
├── report/
├── profile/
└── settings/
```

---

## 12. Kiến trúc từng feature

### 12.1. Splash

```
feature/splash/
├── SplashScreen.kt
├── SplashViewModel.kt
├── SplashUiState.kt
└── SplashEffect.kt      ← dùng Effect thay Event
```

Logic Splash:

```
[Start]
  → Refresh token nếu có
  → firstLoginRequired? → FirstLoginPassword
  → currentSpaceId null? → SpaceSelection
  → role = OWNER → OwnerHome
  → role = TENANT → TenantHome
```

---

### 12.2. Auth

```
feature/auth/
├── login/
│   ├── LoginScreen.kt
│   ├── LoginViewModel.kt
│   ├── LoginUiState.kt
│   ├── LoginEvent.kt
│   └── LoginEffect.kt
│
├── first_login/
│   ├── FirstLoginChangePasswordScreen.kt
│   ├── FirstLoginChangePasswordViewModel.kt
│   ├── FirstLoginChangePasswordUiState.kt
│   └── FirstLoginChangePasswordEvent.kt
│
├── forgot_password/
│   ├── ForgotPasswordScreen.kt
│   ├── ForgotPasswordViewModel.kt
│   └── ForgotPasswordUiState.kt
│
└── component/
    ├── PhoneInputField.kt
    ├── PasswordInputField.kt
    ├── AuthHeader.kt
    └── AuthButton.kt
```

---

### 12.3. Space

```
feature/space/
├── list/
│   ├── SpaceSelectionScreen.kt
│   ├── SpaceSelectionViewModel.kt
│   ├── SpaceSelectionUiState.kt
│   └── SpaceSelectionEffect.kt
│
├── create/
│   ├── CreateSpaceScreen.kt
│   ├── CreateSpaceViewModel.kt
│   ├── CreateSpaceUiState.kt
│   └── CreateSpaceEvent.kt
│
└── component/
    ├── SpaceCard.kt
    ├── SpaceRoleBadge.kt
    └── EmptySpaceView.kt
```

---

### 12.4. Owner Home

```
feature/owner_home/
├── OwnerHomeScreen.kt
├── OwnerHomeViewModel.kt
├── OwnerHomeUiState.kt
└── component/
    ├── RevenueSummaryCard.kt
    ├── OccupancyCard.kt
    ├── PendingInvoiceCard.kt
    ├── RecentTicketCard.kt
    ├── QuickActionGrid.kt
    └── MonthSelector.kt
```

**Widget hiển thị:**

| Widget | Dữ liệu |
|--------|---------|
| RevenueSummaryCard | Tổng doanh thu / Tiền chưa thu tháng |
| OccupancyCard | Tỷ lệ lấp đầy, phòng trống vs. đang thuê |
| PendingInvoiceCard | Số hóa đơn chờ xác nhận |
| RecentTicketCard | Ticket mới nhất chưa xử lý |
| QuickActionGrid | Nhập điện nước, Phát hành HĐ, Thêm phòng |

---

### 12.5. Tenant Home

```
feature/tenant_home/
├── TenantHomeScreen.kt
├── TenantHomeViewModel.kt
├── TenantHomeUiState.kt
└── component/
    ├── CurrentInvoiceCard.kt
    ├── RoomInfoCard.kt
    ├── AnnouncementPreviewCard.kt
    ├── PaymentStatusCard.kt
    └── QuickTenantActionGrid.kt
```

---

### 12.6. Room

```
feature/room/
├── list/
│   ├── RoomListScreen.kt
│   ├── RoomListViewModel.kt
│   ├── RoomListUiState.kt
│   └── RoomListEvent.kt
│
├── detail/
│   ├── RoomDetailScreen.kt
│   ├── RoomDetailViewModel.kt
│   ├── RoomDetailUiState.kt
│   └── RoomDetailEvent.kt
│
├── create_edit/
│   ├── CreateEditRoomScreen.kt
│   ├── CreateEditRoomViewModel.kt
│   ├── CreateEditRoomUiState.kt
│   └── CreateEditRoomEvent.kt
│
└── component/
    ├── RoomCard.kt
    ├── RoomStatusBadge.kt
    ├── RoomMemberList.kt
    ├── RoomPriceSection.kt
    ├── RoomFilterBar.kt       ← ⚡ Mới
    └── EmptyRoomView.kt
```

---

### 12.7. Tenant Management

```
feature/tenant/
├── list/
├── detail/
├── add/
├── assign_room/
└── component/
    ├── TenantCard.kt
    ├── TenantAvatar.kt
    ├── TenantStatusBadge.kt
    └── RoomMemberItem.kt
```

---

### 12.8. Billing

```
feature/billing/
├── dashboard/
├── meter/
├── rule/
├── share_ratio/
├── preview/
└── component/
    ├── MeterInputCard.kt
    ├── BillingRuleCard.kt
    ├── ShareRatioItem.kt
    ├── InvoicePreviewCard.kt
    ├── FeeBreakdownTable.kt
    └── BillingMonthPicker.kt
```

**Luồng phát hành hóa đơn:**

```
1. Chọn tháng tính hóa đơn
2. Chọn phòng / toàn bộ dãy trọ
3. Nhập chỉ số điện nước
4. Kiểm tra BillingRule (cấu hình đơn giá)
5. Kiểm tra ShareRatio
6. Xem trước hóa đơn → [Chỉnh sửa nếu cần]
7. Xác nhận phát hành
8. Backend xử lý async → UI hiển thị trạng thái PROCESSING
9. Nhận FCM notification khi hoàn tất
10. Refresh danh sách hóa đơn
```

---

### 12.9. Invoice

```
feature/invoice/
├── list/
├── detail/
├── history/
└── component/
    ├── InvoiceCard.kt
    ├── InvoiceStatusBadge.kt
    ├── InvoiceDetailRow.kt
    ├── InvoiceSummaryCard.kt
    ├── InvoiceFeeBreakdown.kt
    ├── InvoiceFilterBar.kt    ← ⚡ Mới
    └── InvoiceTimeline.kt
```

---

### 12.10. Payment

```
feature/payment/
├── list/
├── detail/
├── create/
└── component/
    ├── PaymentMethodCard.kt
    ├── PaymentStatusBadge.kt
    ├── PaymentProofUploader.kt
    └── PaymentHistoryItem.kt
```

---

### 12.11. Community

```
feature/community/
├── feed/
├── detail/
├── create/
├── comment/
└── component/
    ├── PostCard.kt
    ├── PostTypeChip.kt
    ├── PinnedAnnouncementCard.kt
    ├── PostImageGrid.kt
    ├── ReactionBar.kt
    └── FeedEmptyView.kt
```

**Quyền đăng bài:**

| Loại bài | Chủ trọ | Người thuê |
|----------|---------|------------|
| ANNOUNCEMENT | ✅ | ❌ |
| MARKETPLACE | ✅ | ✅ |
| DISCUSSION | ✅ | ✅ |
| DEVICE_REPORT | ✅ | ✅ |

---

### 12.12. Ticket

```
feature/ticket/
├── list/
├── detail/
├── create/
└── component/
    ├── TicketCard.kt
    ├── TicketStatusBadge.kt
    ├── TicketPriorityBadge.kt   ← ⚡ Mới
    ├── TicketImageUploader.kt
    └── TicketTimeline.kt
```

---

### 12.13. Chat

```
feature/chat/
├── room_list/
├── conversation/
└── component/
    ├── ChatRoomItem.kt
    ├── MessageBubble.kt
    ├── MessageInputBar.kt
    ├── TypingIndicator.kt
    ├── MessageTimeLabel.kt
    ├── MessageStatusIcon.kt     ← ⚡ Mới (sent/delivered/read)
    └── ChatAttachmentPicker.kt
```

**Luồng chat:**

```
1. Load danh sách phòng chat
2. Chọn phòng chat
3. Load tin nhắn từ Room cache (hiển thị ngay)
4. Gọi API lấy tin nhắn mới hơn
5. Kết nối Realtime (WebSocket / Firebase)
6. Gửi tin nhắn → lưu optimistically vào Room
7. Nhận tin nhắn → append vào list + lưu Room
8. Disconnect khi rời màn hình
```

---

### 12.14. Notification

```
feature/notification/
├── list/
├── detail/
└── component/
    ├── NotificationItem.kt
    ├── NotificationTypeIcon.kt
    └── EmptyNotificationView.kt
```

---

### 12.15. Report

```
feature/report/
├── ReportDashboardScreen.kt
├── ReportDashboardViewModel.kt
├── ReportDashboardUiState.kt
└── component/
    ├── RevenueChart.kt          ← dùng Vico hoặc MPAndroidChart
    ├── OccupancyChart.kt
    ├── DebtSummaryCard.kt
    ├── MonthlyRevenueCard.kt
    ├── ExportReportButton.kt
    └── ChartLegend.kt           ← ⚡ Mới
```

---

### 12.16. Profile

```
feature/profile/
├── ProfileScreen.kt
├── ProfileViewModel.kt
├── ProfileUiState.kt
├── EditProfileScreen.kt
├── ChangePasswordScreen.kt
└── component/
    ├── ProfileHeader.kt
    ├── ProfileMenuItem.kt
    ├── AvatarUploader.kt
    └── RoleInfoCard.kt
```

---

### 12.17. Settings

```
feature/settings/
├── SettingsScreen.kt
├── SettingsViewModel.kt
├── SettingsUiState.kt
└── component/
    ├── SettingSwitchItem.kt
    ├── SettingSelectItem.kt
    ├── LanguageSelectItem.kt    ← ⚡ Mới (nếu hỗ trợ đa ngôn ngữ)
    └── AppVersionItem.kt
```

---

## 13. Chuẩn ViewModel, UiState, Event, Effect

### Cấu trúc file chuẩn

```
LoginScreen.kt         ← UI thuần, không logic
LoginViewModel.kt      ← Xử lý event, emit state/effect
LoginUiState.kt        ← Dữ liệu hiển thị
LoginEvent.kt          ← Intent từ UI
LoginEffect.kt         ← Side effects (navigate, toast)
```

### UiState mẫu

```kotlin
data class LoginUiState(
    val phone: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val phoneError: UiText? = null,      // ⚡ Mới — field-level error
    val passwordError: UiText? = null,
    val generalError: UiText? = null
)
```

### Event mẫu

```kotlin
sealed class LoginEvent {
    data class PhoneChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    data object TogglePasswordVisibility : LoginEvent()
    data object Submit : LoginEvent()
    data object DismissError : LoginEvent()    // ⚡ Mới
}
```

### Effect mẫu

```kotlin
sealed class LoginEffect {
    data object NavigateToOwnerHome : LoginEffect()
    data object NavigateToTenantHome : LoginEffect()
    data object NavigateToFirstLoginPassword : LoginEffect()
    data class ShowToast(val message: UiText) : LoginEffect()  // ⚡ UiText
}
```

### ViewModel mẫu

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.PhoneChanged ->
                _uiState.update { it.copy(phone = event.value, phoneError = null) }
            is LoginEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.value, passwordError = null) }
            LoginEvent.TogglePasswordVisibility ->
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            LoginEvent.Submit -> submitLogin()
            LoginEvent.DismissError ->
                _uiState.update { it.copy(generalError = null) }
        }
    }

    private fun submitLogin() {
        val state = _uiState.value
        launchWithLoading(
            onError = { error -> _uiState.update { it.copy(isLoading = false, generalError = error) } }
        ) {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = loginUseCase(state.phone, state.password)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    val user = result.data
                    if (user.firstLoginRequired)
                        _effect.send(LoginEffect.NavigateToFirstLoginPassword)
                    else
                        _effect.send(LoginEffect.NavigateToOwnerHome) // hoặc Tenant
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, generalError = result.exception.toUiText()) }
                }
                else -> Unit
            }
        }
    }
}
```

---

## 14. Chuẩn xử lý trạng thái màn hình

Mỗi màn hình xử lý đủ 4 trạng thái:

```
Loading → Success → Empty (nếu rỗng) / Error (nếu lỗi)
```

**Ví dụ màn hình danh sách phòng:**

| Trạng thái | Hiển thị | Hành động |
|------------|----------|-----------|
| Loading | ShimmerCard (skeleton) | — |
| Success | Danh sách RoomCard | Pull-to-refresh |
| Empty | EmptyView "Chưa có phòng nào" + nút Thêm phòng | Button CTA |
| Error | ErrorView + nút Thử lại | Retry → gọi lại API |

**Composable pattern:**

```kotlin
@Composable
fun RoomListScreen(
    uiState: RoomListUiState,
    onEvent: (RoomListEvent) -> Unit
) {
    when {
        uiState.isLoading -> ShimmerRoomList()
        uiState.error != null -> ErrorView(
            message = uiState.error.asString(),
            onRetry = { onEvent(RoomListEvent.Retry) }
        )
        uiState.rooms.isEmpty() -> EmptyView(
            title = "Chưa có phòng nào",
            action = { onEvent(RoomListEvent.CreateRoom) }
        )
        else -> RoomList(
            rooms = uiState.rooms,
            onRoomClick = { onEvent(RoomListEvent.OpenRoom(it)) }
        )
    }
}
```

---

## 15. Error Handling Pattern

### 15.1. Phân loại lỗi

| Loại lỗi | Xử lý |
|----------|-------|
| **Network Error** (mất mạng) | Toast + offline indicator |
| **HTTP 401** (hết token) | Tự động refresh → retry, nếu fail → về Login |
| **HTTP 403** (không có quyền) | Dialog thông báo + về Home |
| **HTTP 404** | Dialog "Không tìm thấy dữ liệu" |
| **HTTP 500** | Dialog "Lỗi server, thử lại sau" |
| **Validation Error** | Hiển thị inline dưới input field |
| **Business Error** | Hiển thị trong ErrorView hoặc Dialog |

### 15.2. GlobalErrorHandler ← ⚡ Mới

```kotlin
object GlobalErrorHandler {
    fun handle(exception: NetworkException, navController: NavController): ErrorAction {
        return when (exception) {
            is NetworkException.HttpError -> when (exception.code) {
                401 -> ErrorAction.Logout
                403 -> ErrorAction.ShowDialog(UiText.StringResource(R.string.error_forbidden))
                else -> ErrorAction.ShowInline(exception.toUiText())
            }
            NetworkException.NetworkError -> ErrorAction.ShowOfflineBanner
            else -> ErrorAction.ShowInline(exception.toUiText())
        }
    }
}

sealed class ErrorAction {
    data object Logout : ErrorAction()
    data object ShowOfflineBanner : ErrorAction()
    data class ShowDialog(val message: UiText) : ErrorAction()
    data class ShowInline(val message: UiText) : ErrorAction()
}
```

### 15.3. Offline Banner

```kotlin
@Composable
fun OfflineBanner(isVisible: Boolean) {
    AnimatedVisibility(visible = isVisible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error)
                .padding(Spacing.sm),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Không có kết nối mạng",
                color = MaterialTheme.colorScheme.onError,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
```

---

## 16. Dependency Injection (Hilt)

### 16.1. `NetworkModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .authenticator(TokenAuthenticator(tokenManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideInvoiceApi(retrofit: Retrofit): InvoiceApi =
        retrofit.create(InvoiceApi::class.java)

    // ... tương tự cho các API khác
}
```

### 16.2. `DatabaseModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "smartrent.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideInvoiceDao(db: AppDatabase): InvoiceDao = db.invoiceDao()
    // ... các DAO khác
}
```

### 16.3. `RepositoryModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindInvoiceRepository(
        impl: InvoiceRepositoryImpl
    ): InvoiceRepository

    // ... tương tự cho các repository khác
}
```

---

## 17. Testing Strategy

### 17.1. Phân tầng test

| Layer | Loại test | Tool |
|-------|-----------|------|
| **UseCase** | Unit Test | JUnit5 + MockK |
| **ViewModel** | Unit Test | JUnit5 + MockK + Turbine |
| **Repository** | Unit Test (mock API/DB) | MockWebServer + Room In-Memory |
| **Mapper** | Unit Test | JUnit5 |
| **UI** | Composable Test | Compose Testing |
| **E2E** | Instrumented Test | Espresso / UI Automator |

### 17.2. ViewModel Test mẫu

```kotlin
@ExtendWith(CoroutinesTestExtension::class)
class LoginViewModelTest {

    private val loginUseCase = mockk<LoginUseCase>()
    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setup() {
        viewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun `when login success, emit NavigateToOwnerHome effect`() = runTest {
        // Arrange
        val user = User(id = "1", fullName = "Test", phone = "0901234567",
            avatarUrl = null, firstLoginRequired = false, createdAt = "")
        coEvery { loginUseCase(any(), any()) } returns Result.Success(user)

        // Act & Assert
        viewModel.effect.test {
            viewModel.onEvent(LoginEvent.PhoneChanged("0901234567"))
            viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
            viewModel.onEvent(LoginEvent.Submit)
            assertThat(awaitItem()).isInstanceOf(LoginEffect.NavigateToOwnerHome::class.java)
        }
    }

    @Test
    fun `when login fails with network error, show error in uiState`() = runTest {
        // Arrange
        coEvery { loginUseCase(any(), any()) } returns
            Result.Error(NetworkException.NetworkError)

        // Act
        viewModel.onEvent(LoginEvent.Submit)

        // Assert
        assertThat(viewModel.uiState.value.generalError).isNotNull()
    }
}
```

### 17.3. Repository Test mẫu

```kotlin
class InvoiceRepositoryTest {
    private val mockServer = MockWebServer()
    private lateinit var api: InvoiceApi
    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java
    ).build()

    @Test
    fun `getInvoiceList returns mapped invoices`() = runTest {
        // Setup mock response
        mockServer.enqueue(MockResponse().setBody(INVOICE_LIST_JSON))

        val result = repository.getInvoiceList("space-1", null, null, null, 1)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.data).hasSize(2)
    }
}
```

### 17.4. Composable Test mẫu

```kotlin
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `submit button disabled when fields empty`() {
        composeRule.setContent {
            LoginScreen(
                uiState = LoginUiState(),
                onEvent = {}
            )
        }
        composeRule.onNodeWithTag("submit_button").assertIsNotEnabled()
    }
}
```

---

## 18. Performance Optimization

### 18.1. Compose Recomposition

```kotlin
// ❌ Sai — lambda không stable, gây recompose không cần thiết
@Composable
fun RoomList(rooms: List<Room>, onRoomClick: (String) -> Unit) {
    LazyColumn {
        items(rooms) { room ->
            RoomCard(room = room, onClick = { onRoomClick(room.id) })
        }
    }
}

// ✅ Đúng — dùng key để tối ưu diff
LazyColumn {
    items(rooms, key = { it.id }) { room ->
        RoomCard(room = room, onClick = { onRoomClick(room.id) })
    }
}
```

### 18.2. State hoisting

```kotlin
// Hoist state lên ViewModel, tránh state trong composable con
@Composable
fun RoomListScreen(viewModel: RoomListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RoomListContent(uiState = uiState, onEvent = viewModel::onEvent)
}

// Composable con hoàn toàn stateless → dễ preview và test
@Composable
private fun RoomListContent(
    uiState: RoomListUiState,
    onEvent: (RoomListEvent) -> Unit
) { /* ... */ }
```

### 18.3. Image loading

```kotlin
// Dùng Coil với diskCache và memoryCaching
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build(),
    contentDescription = null,
    contentScale = ContentScale.Crop
)
```

### 18.4. Pagination

Dùng Paging 3 cho danh sách dài (hóa đơn, tin nhắn, thông báo):

```kotlin
class InvoicePagingSource(
    private val api: InvoiceApi,
    private val spaceId: String
) : PagingSource<Int, InvoiceDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InvoiceDto> {
        val page = params.key ?: 1
        return try {
            val response = api.getInvoiceList(spaceId, null, null, null, page)
            LoadResult.Page(
                data = response.data.items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.data.hasNext) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
```

### 18.5. Startup Performance

* Dùng `SplashScreen API` (Android 12+) để tránh white flash
* Lazy inject các dependency không cần ngay
* Defer non-critical initialization bằng `App Startup` library

---

## 19. Accessibility

### 19.1. Checklist bắt buộc

| Yêu cầu | Triển khai |
|---------|-----------|
| Content description cho icon | `contentDescription = "..."` |
| Minimum touch target 48dp | `Modifier.minimumInteractiveComponentSize()` |
| Contrast ratio ≥ 4.5:1 | Kiểm tra với tool Material Color |
| Hỗ trợ font scale | Dùng `sp` cho text, kiểm tra với 200% font scale |
| TalkBack compatible | Test với TalkBack bật |
| Focus order hợp lý | `Modifier.semantics { traversalIndex = ... }` |

### 19.2. Semantic mẫu

```kotlin
@Composable
fun InvoiceStatusBadge(status: InvoiceStatus) {
    Box(
        modifier = Modifier.semantics {
            contentDescription = "Trạng thái hóa đơn: ${status.toVietnamese()}"
        }
    ) {
        Text(text = status.toVietnamese(), color = status.color)
    }
}
```

---

## 20. DeepLink Handling

### 20.1. Các DeepLink cần hỗ trợ

| Intent | DeepLink |
|--------|----------|
| Mở hóa đơn cụ thể | `smartrent://invoice/{invoiceId}` |
| Mở ticket cụ thể | `smartrent://ticket/{ticketId}` |
| Mở phòng chat | `smartrent://chat/{chatRoomId}` |
| Thông báo → Hóa đơn | `smartrent://invoice/{invoiceId}` |

### 20.2. Khai báo trong NavGraph

```kotlin
composable(
    route = Screen.InvoiceDetail.route,
    deepLinks = listOf(
        navDeepLink {
            uriPattern = "smartrent://invoice/{invoiceId}"
        }
    )
) { backStackEntry ->
    val invoiceId = backStackEntry.arguments?.getString("invoiceId") ?: return@composable
    InvoiceDetailScreen(invoiceId = invoiceId)
}
```

### 20.3. Handle từ FCM notification

```kotlin
// Trong MainActivity
override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    navController.handleDeepLink(intent)
}
```

---

## 21. Quy tắc phân quyền frontend

Frontend ẩn/hiện UI theo vai trò, nhưng **backend luôn là nơi kiểm tra quyền thật sự**.

| Chức năng | Chủ trọ | Người thuê |
|-----------|---------|------------|
| Tạo dãy trọ | ✅ | ❌ |
| Tạo/sửa phòng | ✅ | ❌ |
| Thêm người thuê | ✅ | ❌ |
| Nhập điện nước | ✅ | ❌ |
| Cấu hình share ratio | ✅ | ❌ |
| Phát hành hóa đơn | ✅ | ❌ |
| Xem hóa đơn cá nhân | ✅ | ✅ |
| Xác nhận thanh toán | ✅ | ❌ |
| Upload minh chứng thanh toán | ❌ | ✅ |
| Đăng thông báo (ANNOUNCEMENT) | ✅ | ❌ |
| Đăng giao lưu/marketplace | ✅ | ✅ |
| Báo lỗi thiết bị | ✅ | ✅ |
| Cập nhật trạng thái ticket | ✅ | ❌ |
| Chat | ✅ | ✅ |
| Xem báo cáo doanh thu | ✅ | ❌ |

**Pattern kiểm tra quyền:**

```kotlin
@Composable
fun OwnerOnly(role: UserRole, content: @Composable () -> Unit) {
    if (role == UserRole.OWNER) content()
}

// Dùng:
OwnerOnly(role = currentRole) {
    CreateRoomButton(onClick = { ... })
}
```

---

## 22. API Mapping theo module

| Feature | API Interface | Repository |
|---------|--------------|------------|
| Auth | `AuthApi.kt` | `AuthRepository` |
| Space | `SpaceApi.kt` | `SpaceRepository` |
| Room | `RoomApi.kt` | `RoomRepository` |
| Tenant | `TenantApi.kt` | `TenantRepository` |
| Billing | `BillingApi.kt` | `BillingRepository` |
| Invoice | `InvoiceApi.kt` | `InvoiceRepository` |
| Payment | `PaymentApi.kt` | `PaymentRepository` |
| Community | `CommunityApi.kt` | `CommunityRepository` |
| Ticket | `TicketApi.kt` | `TicketRepository` |
| Chat | `ChatApi.kt` | `ChatRepository` |
| Notification | `NotificationApi.kt` | `NotificationRepository` |
| Report | `ReportApi.kt` | `ReportRepository` |
| File Upload | `FileApi.kt` | `FileRepository` |

---

## 23. Upload ảnh

### Module cần upload

| Module | Loại ảnh | Max size |
|--------|----------|----------|
| Profile | Ảnh đại diện | 5MB |
| Community | Ảnh bài viết (max 5 ảnh) | 10MB/ảnh |
| Ticket | Ảnh thiết bị hỏng (max 3 ảnh) | 10MB/ảnh |
| Payment | Minh chứng chuyển khoản | 10MB |

### Luồng upload (Presigned URL)

```
1. User chọn ảnh từ gallery/camera
2. App kiểm tra định dạng (jpg, png, webp) và dung lượng
3. App compress/resize nếu cần (Coil + BitmapFactory)
4. App gọi API xin presigned URL từ backend
5. App PUT ảnh trực tiếp lên S3/storage bằng presigned URL
6. Storage trả về fileUrl
7. App gửi fileUrl về backend cùng với data của form
```

### `ImageUtils.kt`

```kotlin
object ImageUtils {
    const val MAX_SIZE_BYTES = 10 * 1024 * 1024 // 10MB
    val ALLOWED_TYPES = setOf("image/jpeg", "image/png", "image/webp")

    fun compressImage(context: Context, uri: Uri, maxSizeKB: Int = 1024): ByteArray {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val output = ByteArrayOutputStream()
        var quality = 90
        do {
            output.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
            quality -= 10
        } while (output.size() > maxSizeKB * 1024 && quality > 10)
        return output.toByteArray()
    }
}
```

---

## 24. Cache local

### Chiến lược cache

| Loại dữ liệu | Strategy | TTL |
|-------------|----------|-----|
| User session | Luôn giữ đến logout | — |
| RentalSpace list | Cache first, refresh khi online | 1 giờ |
| Room list | Cache first, refresh on pull | 30 phút |
| Invoice list | Cache page 1, fetch newer | 15 phút |
| CommunityPost | Cache 20 bài mới nhất | 10 phút |
| Message | Cache 50 tin nhắn mới nhất/phòng | Vô hạn (chat history) |
| Notification | Cache 50 thông báo mới nhất | 1 giờ |

### Database schema (Room)

```kotlin
@Database(
    entities = [
        UserEntity::class,
        RentalSpaceEntity::class,
        RoomEntity::class,
        InvoiceEntity::class,
        CommunityPostEntity::class,
        MessageEntity::class,
        NotificationEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun roomDao(): RoomDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao
}
```

---

## 25. Realtime và Notification

### 25.1. Kiến trúc Realtime

```
core/realtime/
├── RealtimeClient.kt          ← Interface (abstraction)
├── WebSocketRealtimeClient.kt ← WebSocket implementation
├── FirebaseRealtimeClient.kt  ← Firebase implementation
└── RealtimeEventHandler.kt    ← Dispatch events đến đúng feature
```

**`RealtimeClient.kt`:**

```kotlin
interface RealtimeClient {
    fun connect(chatRoomId: String)
    fun disconnect()
    fun sendMessage(message: SendMessageRequest)
    val events: Flow<RealtimeEvent>
}

sealed class RealtimeEvent {
    data class NewMessage(val message: Message) : RealtimeEvent()
    data class TypingStarted(val userId: String) : RealtimeEvent()
    data class TypingStopped(val userId: String) : RealtimeEvent()
    data class MessageRead(val messageId: String) : RealtimeEvent()
}
```

### 25.2. Push Notification (FCM)

**Các loại notification và deep link tương ứng:**

| Sự kiện | Deep link | Đối tượng nhận |
|---------|-----------|----------------|
| Hóa đơn mới | `smartrent://invoice/{id}` | Tenant |
| Thanh toán xác nhận | `smartrent://invoice/{id}` | Tenant |
| Thanh toán từ chối | `smartrent://invoice/{id}` | Tenant |
| Tin nhắn mới | `smartrent://chat/{chatRoomId}` | Tất cả |
| Thông báo mới | `smartrent://notification/{id}` | Tất cả |
| Ticket cập nhật | `smartrent://ticket/{id}` | Tất cả |
| Được thêm vào phòng | `smartrent://room/{id}` | Tenant |

**`SmartRentFirebaseMessagingService.kt`:**

```kotlin
class SmartRentFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val type = message.data["type"] ?: return
        val targetId = message.data["targetId"] ?: ""

        val deepLink = when (type) {
            "NEW_INVOICE" -> "smartrent://invoice/$targetId"
            "NEW_MESSAGE" -> "smartrent://chat/$targetId"
            "TICKET_UPDATED" -> "smartrent://ticket/$targetId"
            else -> null
        }

        showNotification(
            title = message.notification?.title ?: "SmartRent",
            body = message.notification?.body ?: "",
            deepLink = deepLink
        )
    }

    override fun onNewToken(token: String) {
        // Gửi token mới lên backend
    }
}
```

---

## 26. Danh sách màn hình đầy đủ

### 26.1. Màn hình chung

| STT | Màn hình | Route |
|-----|----------|-------|
| 1 | Splash | `splash` |
| 2 | Login | `login` |
| 3 | First Login Change Password | `first_login_password` |
| 4 | Forgot Password | `forgot_password` |
| 5 | Space Selection | `space_selection` |
| 6 | Profile | `profile` |
| 7 | Edit Profile | `edit_profile` |
| 8 | Change Password | `change_password` |
| 9 | Settings | `settings` |
| 10 | Notification List | `notifications` |

### 26.2. Màn hình Chủ trọ (28 màn hình)

| STT | Màn hình | Route |
|-----|----------|-------|
| 1 | Owner Home | `owner_home` |
| 2 | Room List | `rooms` |
| 3 | Room Detail | `rooms/{roomId}` |
| 4 | Create Room | `rooms/create` |
| 5 | Edit Room | `rooms/{roomId}/edit` |
| 6 | Tenant List | `tenants` |
| 7 | Tenant Detail | `tenants/{tenantId}` |
| 8 | Add Tenant | `tenants/add` |
| 9 | Assign Tenant To Room | `tenants/{tenantId}/assign` |
| 10 | Billing Dashboard | `billing` |
| 11 | Meter Reading | `billing/meter` |
| 12 | Billing Rule | `billing/rules` |
| 13 | Share Ratio | `billing/share-ratio` |
| 14 | Invoice Preview | `billing/preview` |
| 15 | Invoice List | `invoices` |
| 16 | Invoice Detail | `invoices/{invoiceId}` |
| 17 | Payment List | `payments` |
| 18 | Payment Detail | `payments/{paymentId}` |
| 19 | Confirm Payment | `payments/{paymentId}/confirm` |
| 20 | Community Feed | `community` |
| 21 | Create Announcement | `community/create?type=ANNOUNCEMENT` |
| 22 | Create Post | `community/create` |
| 23 | Post Detail | `community/{postId}` |
| 24 | Ticket List | `tickets` |
| 25 | Ticket Detail | `tickets/{ticketId}` |
| 26 | Chat Room List | `chats` |
| 27 | Chat Conversation | `chats/{chatRoomId}` |
| 28 | Report Dashboard | `reports` |

### 26.3. Màn hình Người thuê (15 màn hình)

| STT | Màn hình | Route |
|-----|----------|-------|
| 1 | Tenant Home | `tenant_home` |
| 2 | My Room | `my-room` |
| 3 | My Invoice | `my-invoices` |
| 4 | Invoice Detail | `my-invoices/{invoiceId}` |
| 5 | Invoice History | `my-invoices/history` |
| 6 | Payment History | `my-payments` |
| 7 | Upload Payment Proof | `my-payments/{invoiceId}/upload` |
| 8 | Community Feed | `community` |
| 9 | Create Post | `community/create` |
| 10 | Post Detail | `community/{postId}` |
| 11 | Create Ticket | `tickets/create` |
| 12 | My Tickets | `my-tickets` |
| 13 | Ticket Detail | `my-tickets/{ticketId}` |
| 14 | Chat Room List | `chats` |
| 15 | Chat Conversation | `chats/{chatRoomId}` |

---

## 27. Thứ tự triển khai Frontend

### Giai đoạn 1 — Nền tảng app (Sprint 1)

```
1. Khởi tạo project + cấu trúc package
2. Cài theme + design tokens
3. Cài shared components (Button, TextField, Card, Loading, Dialog)
4. Cài Navigation graph
5. Cài Retrofit + OkHttp + AuthInterceptor
6. Cài DataStore
7. Cài Room Database
8. Cài Result + UiText chuẩn
9. Cài Hilt modules
```

### Giai đoạn 2 — Auth và session (Sprint 1-2)

```
1. SplashScreen (+ logic routing)
2. LoginScreen
3. TokenAuthenticator (refresh token tự động)
4. FirstLoginChangePasswordScreen
5. LogoutFlow
6. SpaceSelectionScreen
```

### Giai đoạn 3 — Chủ trọ quản lý phòng/người thuê (Sprint 2-3)

```
1. OwnerHomeScreen (summary cards)
2. RoomListScreen
3. RoomDetailScreen
4. CreateEditRoomScreen
5. TenantListScreen
6. AddTenantScreen
7. AssignTenantToRoomScreen
```

### Giai đoạn 4 — Billing và Invoice (Sprint 3-4)

```
1. BillingDashboardScreen
2. MeterReadingScreen
3. BillingRuleScreen
4. ShareRatioScreen
5. InvoicePreviewScreen
6. PublishInvoice flow
7. InvoiceListScreen (Owner)
8. InvoiceDetailScreen
```

### Giai đoạn 5 — Người thuê (Sprint 4-5)

```
1. TenantHomeScreen
2. MyRoomScreen
3. MyInvoiceScreen
4. InvoiceDetailScreen (Tenant view)
5. InvoiceHistoryScreen
6. UploadPaymentProofScreen
7. PaymentHistoryScreen
8. ConfirmPayment flow (Owner)
```

### Giai đoạn 6 — Community và Ticket (Sprint 5-6)

```
1. CommunityFeedScreen
2. CreatePostScreen
3. PostDetailScreen
4. CommentSection
5. CreateTicketScreen
6. TicketListScreen
7. TicketDetailScreen
8. UpdateTicketStatusFlow
```

### Giai đoạn 7 — Chat, Notification, Report (Sprint 6-7)

```
1. ChatRoomListScreen
2. ChatConversationScreen
3. Local message cache
4. Realtime chat integration
5. NotificationListScreen
6. FCM push notification
7. DeepLink handling
8. ReportDashboardScreen
```

---

## 28. Phạm vi MVP Frontend

### Modules cần thiết cho MVP

```
Auth               → Đăng nhập, đổi mật khẩu lần đầu
Space              → Chọn dãy trọ
Owner Home         → Tổng quan cho chủ trọ
Tenant Home        → Tổng quan cho người thuê
Room               → Quản lý phòng
Tenant Management  → Quản lý người thuê
Billing            → Chốt điện nước, phát hành hóa đơn
Invoice            → Xem danh sách và chi tiết hóa đơn
Payment cơ bản     → Upload minh chứng + xác nhận
Community cơ bản   → Feed và đăng bài
Ticket cơ bản      → Tạo và xem ticket
Profile            → Thông tin cá nhân
```

### 21 màn hình MVP

| STT | Màn hình | Ưu tiên |
|-----|----------|---------|
| 1 | Splash | 🔴 P0 |
| 2 | Login | 🔴 P0 |
| 3 | First Login Change Password | 🔴 P0 |
| 4 | Space Selection | 🔴 P0 |
| 5 | Owner Home | 🔴 P0 |
| 6 | Tenant Home | 🔴 P0 |
| 7 | Room List | 🔴 P0 |
| 8 | Room Detail | 🟠 P1 |
| 9 | Create/Edit Room | 🟠 P1 |
| 10 | Tenant List | 🔴 P0 |
| 11 | Add Tenant | 🔴 P0 |
| 12 | Meter Reading | 🔴 P0 |
| 13 | Share Ratio | 🟠 P1 |
| 14 | Invoice Preview | 🔴 P0 |
| 15 | Invoice List | 🔴 P0 |
| 16 | Invoice Detail | 🔴 P0 |
| 17 | Upload Payment Proof | 🔴 P0 |
| 18 | Community Feed | 🟡 P2 |
| 19 | Create Ticket | 🟠 P1 |
| 20 | Ticket Detail | 🟠 P1 |
| 21 | Profile | 🟠 P1 |

> Chat realtime, notification nâng cao, biểu đồ báo cáo — để sau MVP.

---

## 29. Tiêu chuẩn hoàn thành màn hình & feature

### Màn hình hoàn chỉnh ✅

```
☐ Screen.kt (stateless composable)
☐ ViewModel.kt
☐ UiState.kt
☐ Event.kt (nếu cần)
☐ Effect.kt (nếu cần)
☐ Loading state (shimmer hoặc progress)
☐ Success state
☐ Empty state
☐ Error state với nút Retry
☐ Gọi qua UseCase, không gọi API trực tiếp
☐ Preview hoặc mock data cho Compose Preview
☐ Kiểm tra quyền nếu màn hình phụ thuộc role
☐ Xử lý back navigation
☐ Content description cho icon (Accessibility)
☐ Unit test ViewModel
```

### Feature hoàn chỉnh ✅

```
☐ Tất cả Screen đầy đủ
☐ ViewModel với đầy đủ state handling
☐ UiState + Event + Effect rõ ràng
☐ UseCase tương ứng (mỗi UseCase một việc)
☐ Repository interface
☐ Repository implementation
☐ API interface
☐ DTO + Request model
☐ Mapper (DTO/Entity → Domain)
☐ Room cache (nếu cần offline)
☐ Navigation route đã đăng ký
☐ Hilt module đã khai báo
☐ Kiểm tra quyền truy cập
☐ Không ảnh hưởng feature khác
☐ Unit tests cho UseCase và ViewModel
```

---

## 30. Kết luận kiến trúc

Frontend SmartRent / RentHub được xây dựng theo kiến trúc **MVVM + Clean Architecture + Feature-first** với các nguyên tắc:

**Tách biệt rõ ràng** giữa UI, business logic và data. Mỗi layer có trách nhiệm riêng, không xâm phạm nhau.

**Offline-first** — App hoạt động ổn khi mạng yếu nhờ Room cache và chiến lược sync rõ ràng.

**Scalable** — Mỗi feature là module độc lập. Thêm feature mới không ảnh hưởng feature cũ.

**Testable** — Domain layer không phụ thuộc Android framework → dễ unit test. ViewModel được test với MockK + Turbine.

**Maintainable** — Naming conventions nhất quán, structure rõ ràng, code review dễ dàng.

### Cấu trúc chuẩn tổng kết

```
core          → Network, Storage, DI, Utils, Constants
data          → API interfaces, DTO, Request, Room Entities, Mapper, Repository Impl
domain        → Domain Model, Repository Interface, UseCase
navigation    → NavGraph, Screen routes, DeepLink
presentation  → Theme, shared Components
feature       → Từng module nghiệp vụ (splash, auth, room, billing, ...)
```

**Ưu tiên triển khai:**
1. 🔴 **P0 — MVP core**: Auth, Space, Room, Tenant, Billing, Invoice, Payment
2. 🟠 **P1 — Hoàn thiện**: Community, Ticket, Profile, Settings
3. 🟡 **P2 — Mở rộng**: Chat realtime, Report dashboard, Notification nâng cao

---

*Document version 2.0 — SmartRent / RentHub Frontend Architecture*