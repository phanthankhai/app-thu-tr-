<?php

use App\Http\Controllers\Api\Admin\OnboardingController;
use App\Http\Controllers\Api\AuthController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\Admin\RoomController;
use App\Http\Controllers\Api\Admin\ContractController;
use App\Http\Controllers\Api\Admin\ServiceController;
use App\Http\Controllers\Api\Admin\InvoiceController;
use App\Http\Controllers\Api\Admin\PaymentController;
use App\Http\Controllers\Api\Admin\IncidentController;
use App\Http\Controllers\Api\Admin\BillController;
use App\Http\Controllers\Api\Admin\TenantController;
use App\Http\Controllers\Api\Admin\DashboardController;

// ==========================================
// 1. Tuyến đường KHÔNG yêu cầu đăng nhập
// ==========================================
Route::prefix('v1/auth')->group(function () {
    Route::post('login', [AuthController::class, 'login']);
    Route::post('register', [AuthController::class, 'register']); // Thêm mới
    Route::post('refresh', [AuthController::class, 'refresh']);
    Route::post('logout', [AuthController::class, 'logout']);
});

// ==========================================
// 2. Tuyến đường YÊU CẦU Access Token hợp lệ
// ==========================================
Route::middleware('auth:api')->prefix('v1')->group(function () {
    
    // Quản lý tài khoản
    Route::prefix('auth')->group(function () {
        Route::post('/first-login-change-password', [AuthController::class, 'firstLoginChangePassword']);
        Route::post('change-password', [AuthController::class, 'changePassword']);
        Route::post('/refresh', [AuthController::class, 'refresh']);
        Route::post('/logout', [AuthController::class, 'logout']);
    });
    
    // Phân quyền Chủ trọ
    Route::prefix('admin')->group(function () {
        Route::post('/tenants/onboard', [OnboardingController::class, 'onboardTenant']);
        Route::get('rooms/{id}', [App\Http\Controllers\Api\Admin\RoomController::class, 'show']);
        
        // Quản lý phòng trọ
        Route::get('/rooms', [RoomController::class, 'index']);          
        Route::post('/rooms', [RoomController::class, 'store']);         
        Route::put('/rooms/{id}', [RoomController::class, 'update']);    
        Route::delete('/rooms/{id}', [RoomController::class, 'destroy']); 
        Route::post('/rooms/{id}/services', [RoomController::class, 'syncServices']);
        Route::post('/bills/{id}/pay', [BillController::class, 'markAsPaid']);
        Route::post('/rooms/add-tenant', [\App\Http\Controllers\Api\Admin\TenantController::class, 'addCoTenant']);
        
        // Quản lý hợp đồng
        Route::get('/contracts', [ContractController::class, 'index']);
        Route::post('/contracts', [ContractController::class, 'store']);
        Route::post('/contracts/{id}/terminate', [ContractController::class, 'terminate']);
        
        // Quản lý dịch vụ
        Route::get('/services', [ServiceController::class, 'index']);
        Route::post('/services', [ServiceController::class, 'store']);
        Route::put('/services/{id}', [ServiceController::class, 'update']);
        Route::delete('/services/{id}', [ServiceController::class, 'destroy']);
        
        // Quản lý hóa đơn
        Route::post('/invoices', [InvoiceController::class, 'store']);
        Route::post('/invoices/{id}/split', [InvoiceController::class, 'splitBill']);

        // ĐÃ SỬA: Loại bỏ tiền tố admin/ bị lặp để khớp với cấu trúc nhóm cha
        Route::get('/payments/pending', [BillController::class, 'getPendingPayments']);
        Route::post('/payments/{id}/approve', [BillController::class, 'approvePayment']);

        // Quản lý Thanh toán (Module 5)
        Route::post('/invoices/{id}/payments', [PaymentController::class, 'store']);

        // Quản lý Báo cáo sự cố (Module 6)
        Route::get('/incidents', [IncidentController::class, 'index']);
        Route::post('/incidents', [IncidentController::class, 'store']);
        Route::put('/incidents/{id}/status', [IncidentController::class, 'updateStatus']);
        
        // Quản lý khách thuê (Tenants)
        Route::get('/tenants', [TenantController::class, 'index']);
        Route::get('/tenants/{id}', [TenantController::class, 'show']);
        Route::put('/tenants/{id}', [TenantController::class, 'update']);
        Route::post('/tenants/{id}/end-rent', [TenantController::class, 'endRent']);

        Route::get('/dashboard', [DashboardController::class, 'index']);

        // Quản lý Hóa đơn (Bills)
        Route::get('/bills', [BillController::class, 'index']);
        Route::post('/bills', [BillController::class, 'store']);
    });

    Route::middleware(['auth:api', 'role:tenant'])->prefix('tenant')->group(function () {
        Route::post('/bills/{id}/notify-payment', [\App\Http\Controllers\Api\Admin\BillController::class, 'tenantNotifyPayment']);
    
        // 1. Lấy thông tin phòng và hóa đơn của chính mình
        Route::get('/my-dashboard', [TenantController::class, 'getMyDashboard']);
        
        // 2. Xem danh sách hóa đơn cá nhân
        Route::get('/my-bills', [TenantController::class, 'getMyBills']);
        
        // 3. Gửi yêu cầu hỗ trợ/báo cáo sự cố
        Route::post('/report-incident', [IncidentController::class, 'store']);
    });
});