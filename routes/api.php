<?php

use App\Http\Controllers\Api\Admin\OnboardingController;
use App\Http\Controllers\Api\AuthController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\Admin\RoomController;

// Tuyến đường không yêu cầu đăng nhập
Route::prefix('v1/auth')->group(function () {
    Route::post('/login', [AuthController::class, 'login']);
});

// Tuyến đường YÊU CẦU có Access Token hợp lệ (đã đăng nhập)
Route::middleware('auth:api')->prefix('v1')->group(function () {
    
    // Các thao tác liên quan đến tài khoản
    Route::prefix('auth')->group(function () {
        Route::post('/first-login-change-password', [AuthController::class, 'firstLoginChangePassword']);
        Route::post('/refresh', [AuthController::class, 'refresh']);
        Route::post('/logout', [AuthController::class, 'logout']);
    });

    // Tuyến đường xử lý các tính năng của phân quyền Chủ trọ (Super Admin)
    Route::prefix('admin')->group(function () {
        Route::post('/tenants/onboard', [OnboardingController::class, 'onboardTenant']);
        Route::get('/rooms', [RoomController::class, 'index']);          // Lấy danh sách
        Route::post('/rooms', [RoomController::class, 'store']);         // Thêm mới
        Route::put('/rooms/{id}', [RoomController::class, 'update']);    // Cập nhật
        Route::delete('/rooms/{id}', [RoomController::class, 'destroy']); // Xóa
    });
    // Tuyến đường xử lý các tính năng của phân quyền Chủ trọ (Super Admin)
Route::prefix('admin')->group(function () {
    Route::post('/tenants/onboard', [OnboardingController::class, 'onboardTenant']);

    // 👉 THÊM DÒNG NÀY VÀO ĐỂ ĐĂNG KÝ API TẠO PHÒNG:
    Route::post('/rooms', [RoomController::class, 'store']);
});
});