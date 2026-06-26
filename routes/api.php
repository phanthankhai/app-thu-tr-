<?php

use App\Http\Controllers\Api\Admin\OnboardingController;
use App\Http\Controllers\Api\AuthController;
use Illuminate\Support\Facades\Route;

// ========================================================
// KHU VỰC 1: KHÔNG YÊU CẦU TOKEN (PUBLIC)
// Dành cho việc Đăng nhập và Chủ trọ tạo tài khoản khách
// ========================================================
Route::prefix('v1')->group(function () {
    // Khách thuê đăng nhập
    Route::post('/auth/login', [AuthController::class, 'login']);
    
    // Chủ trọ thêm khách vào phòng (Tạm thời để ở đây để test)
    Route::post('/admin/tenants/onboard', [OnboardingController::class, 'onboardTenant']);
});


// ========================================================
// KHU VỰC 2: BẮT BUỘC PHẢI CÓ TOKEN (PRIVATE)
// Những ai đã đăng nhập thành công mới được vào đây
// ========================================================
Route::middleware('auth:api')->prefix('v1')->group(function () {
    
    Route::prefix('auth')->group(function () {
        // Đổi mật khẩu lần đầu
        Route::post('/first-login-change-password', [AuthController::class, 'firstLoginChangePassword']);
        // Cấp lại token mới
        Route::post('/refresh', [AuthController::class, 'refresh']);
        // Đăng xuất
        Route::post('/logout', [AuthController::class, 'logout']);
    });

});