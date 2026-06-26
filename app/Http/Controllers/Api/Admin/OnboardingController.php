<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class OnboardingController extends Controller
{
    /**
     * API Chủ trọ thêm thành viên vào phòng (Zero-Friction Onboarding)
     */
    public function onboardTenant(Request $request)
    {
        // 1. Xác thực dữ liệu đầu vào từ phía ứng dụng của Chủ trọ
        $validator = Validator::make($request->all(), [
            'phone' => 'required|string|regex:/^([0-9\s\-\+\(\)]*)$/|min:10|unique:users,phone',
            'room_id' => 'required|integer|exists:rooms,id', // Đảm bảo ID phòng trọ có tồn tại
            'name' => 'required|string|max:255',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Dữ liệu không hợp lệ.',
                'errors' => $validator->errors()
            ], 422);
        }

        try {
            // 2. Thuật toán sinh mật khẩu tạm thời (Mã số ngẫu nhiên 6 chữ số làm OTP)
            $temporaryPassword = (string) rand(100000, 999999);

            // 3. Tiến hành khởi tạo tài khoản tự động trong Database
            $tenant = User::create([
                'name' => $request->input('name'),
                'phone' => $request->input('phone'), // Tên đăng nhập cố định
                'room_id' => $request->input('room_id'), // Gán trực tiếp vào phòng tương ứng
                'role' => 'tenant', // Thiết lập vai trò mặc định là người thuê trọ
                'password' => Hash::make($temporaryPassword), // Băm mật khẩu tạm bảo mật
                'is_first_login' => true, // Đặt cờ bắt buộc đổi mật khẩu khi đăng nhập
            ]);

            // 4. Giả lập luồng gửi Mật khẩu tạm thời qua hệ thống SMS/Email định tuyến ngầm
            // Trong thực tế, bạn sẽ dispatch một Job/Event ở đây để gửi SMS qua bên thứ 3 (Twilio, Stringee, SpeedSMS)
            $this->sendTemporaryPasswordViaSMS($tenant->phone, $temporaryPassword);

            // 5. Phản hồi trạng thái thành công ngay lập tức để không treo ứng dụng
            return response()->json([
                'success' => true,
                'message' => 'Khởi tạo tài khoản người thuê thành công. Mật khẩu tạm đã được gửi đi.',
                'data' => [
                    'id' => $tenant->id,
                    'phone' => $tenant->phone,
                    'room_id' => $tenant->room_id,
                    'is_first_login' => $tenant->is_first_login
                ]
            ], 201);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Đã xảy ra lỗi trong quá trình xử lý hệ thống.',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * Hàm phụ trợ giả lập gửi SMS OTP/Mật khẩu tạm thời
     */
    private function sendTemporaryPasswordViaSMS($phone, $password)
    {
        // Logic tích hợp SDK của nhà mạng gửi SMS tin nhắn thương hiệu ở đây
        // Ví dụ: Log::info("Đã gửi mật khẩu tạm [{$password}] đến số điện thoại [{$phone}].");
    }
}