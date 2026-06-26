<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class AuthController extends Controller
{
    /**
     * API Đăng nhập bằng Số điện thoại và Mật khẩu
     */
    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'phone' => 'required|string',
            'password' => 'required|string',
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $credentials = $request->only('phone', 'password');

        /** @var \PHPOpenSourceSaver\JWTAuth\JWTGuard $auth */
        $auth = auth('api');

        // Xác thực người dùng và tạo Access Token
        if (! $token = $auth->attempt($credentials)) {
            return response()->json(['error' => 'Số điện thoại hoặc mật khẩu không chính xác'], 401);
        }

        /** @var \App\Models\User $user */
        $user = $auth->user();

        // Trả về Token và thông tin user, đính kèm cờ is_first_login
        return response()->json([
            'success' => true,
            'message' => $user->is_first_login ? 'Đăng nhập thành công. Vui lòng đổi mật khẩu mới.' : 'Đăng nhập thành công.',
            'data' => [
                'user' => $user,
                'is_first_login' => $user->is_first_login,
                'authorization' => [
                    'access_token' => $token,
                    'type' => 'bearer',
                    'expires_in' => $auth->factory()->getTTL() * 60 // Thời gian sống của Access Token
                ]
            ]
        ]);
    }

    /**
     * API Bắt buộc đổi mật khẩu ở lần đăng nhập đầu (First-login)
     */
    public function firstLoginChangePassword(Request $request)
    {
        /** @var \App\Models\User $user */
        $user = auth('api')->user();

        // Kiểm tra xem người dùng có đúng là đang ở trạng thái first_login không
        if (!$user->is_first_login) {
            return response()->json(['error' => 'Tài khoản này đã thiết lập mật khẩu trước đó.'], 403);
        }

        $validator = Validator::make($request->all(), [
            'new_password' => 'required|string|min:6|confirmed', // Client phải gửi new_password và new_password_confirmation
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        // Cập nhật mật khẩu mới và tắt cờ is_first_login
        $user->password = Hash::make($request->new_password);
        $user->is_first_login = false;
        $user->save();

        return response()->json([
            'success' => true,
            'message' => 'Thiết lập mật khẩu mới thành công. Bạn có thể sử dụng ứng dụng ngay bây giờ.'
        ]);
    }

    /**
     * API Refresh Token (Xin cấp lại Access Token mới khi cái cũ hết hạn)
     */
    public function refresh()
    {
        /** @var \PHPOpenSourceSaver\JWTAuth\JWTGuard $auth */
        $auth = auth('api');

        return response()->json([
            'success' => true,
            'authorization' => [
                'access_token' => $auth->refresh(),
                'type' => 'bearer',
                'expires_in' => $auth->factory()->getTTL() * 60
            ]
        ]);
    }

    /**
     * API Đăng xuất
     */
    public function logout()
    {
        /** @var \PHPOpenSourceSaver\JWTAuth\JWTGuard $auth */
        $auth = auth('api');
        
        $auth->logout();
        
        return response()->json([
            'success' => true,
            'message' => 'Đăng xuất thành công'
        ]);
    }
}