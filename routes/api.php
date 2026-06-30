<?php

use App\Http\Controllers\Api\Admin\OnboardingController;
use App\Http\Controllers\Api\AuthController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\Admin\RoomController;
use App\Http\Controllers\Api\Admin\ContractController;

// ==========================================
// 1. Tuyến đường KHÔNG yêu cầu đăng nhập
// ==========================================
Route::prefix('v1/auth')->group(function () {
    Route::post('/login', [AuthController::class, 'login']);
});

// ==========================================
// 2. Tuyến đường YÊU CẦU Access Token hợp lệ
// ==========================================
Route::middleware('auth:api')->prefix('v1')->group(function () {
    
    // Quản lý tài khoản
    Route::prefix('auth')->group(function () {
        Route::post('/first-login-change-password', [AuthController::class, 'firstLoginChangePassword']);
        Route::post('/refresh', [AuthController::class, 'refresh']);
        Route::post('/logout', [AuthController::class, 'logout']);
    });

    // Phân quyền Chủ trọ
    Route::prefix('admin')->group(function () {
        Route::post('/tenants/onboard', [OnboardingController::class, 'onboardTenant']);
        
        // Quản lý phòng trọ
        Route::get('/rooms', [RoomController::class, 'index']);          
        Route::post('/rooms', [RoomController::class, 'store']);         
        Route::put('/rooms/{id}', [RoomController::class, 'update']);    
        Route::delete('/rooms/{id}', [RoomController::class, 'destroy']); 
        
        // Quản lý hợp đồng
        Route::post('/contracts', [ContractController::class, 'store']);
        // Quản lý hợp đồng
        Route::get('/contracts', [ContractController::class, 'index']);
    });
});