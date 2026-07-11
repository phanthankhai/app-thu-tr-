<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;

class OnboardingController extends Controller
{
    /**
     * API Chủ trọ thêm thành viên vào phòng
     */
    public function onboardTenant(Request $request)
    {
        // 1. Kiểm tra quyền Admin
        if (auth('api')->user()->role !== 'admin') {
            return response()->json(['success' => false, 'message' => 'Bạn không có quyền thực hiện hành động này.'], 403);
        }

        // 2. Xác thực dữ liệu
        $validator = Validator::make($request->all(), [
            'phone'   => 'required|string|unique:users,phone',
            'room_id' => 'required|integer|exists:rooms,id',
            'name'    => 'required|string|max:255',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false, 
                'message' => 'Dữ liệu không hợp lệ.', 
                'errors'  => $validator->errors()
            ], 422);
        }

        try {
            // 3. Sinh mật khẩu tạm (6 chữ số)
            $temporaryPassword = (string) rand(100000, 999999);

            // Ghi log để bạn dễ dàng debug
            Log::info('Admin Onboard khách thuê - SĐT: ' . $request->phone . ' | Mật khẩu tạm thời: ' . $temporaryPassword);

            // 4. Tạo tài khoản Tenant
            $tenant = User::create([
                'name'           => $request->name,
                'phone'          => $request->phone,
                'room_id'        => $request->room_id,
                'role'           => 'tenant',
                'password'       => Hash::make($temporaryPassword),
                'is_first_login' => true,
            ]);

            // 5. Gửi SMS (hàm này sẽ tự ghi log tiếp)
            $this->sendTemporaryPasswordViaSMS($tenant->phone, $temporaryPassword);

            return response()->json([
                'success' => true,
                'message' => 'Onboard khách thành công.',
                'data'    => [
                    'phone'    => $tenant->phone,
                    'temp_pwd' => $temporaryPassword
                ]
            ], 201);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false, 
                'message' => 'Lỗi hệ thống: ' . $e->getMessage()
            ], 500);
        }
    }

    private function sendTemporaryPasswordViaSMS($phone, $password)
    {
        // Log vào file storage/logs/laravel.log để xem mật khẩu
        Log::info("SMS to {$phone}: Tài khoản SmartRent - Mật khẩu: {$password}");
    }
}