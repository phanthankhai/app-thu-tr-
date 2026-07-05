<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use App\Models\User;
use PHPOpenSourceSaver\JWTAuth\Facades\JWTAuth;

class AuthController extends Controller
{
    /**
     * API Đăng nhập
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

        if (! $token = $auth->attempt($credentials)) {
            return response()->json(['error' => 'Số điện thoại hoặc mật khẩu không chính xác'], 401);
        }

        /** @var \App\Models\User $user */
        $user = $auth->user();

        // Trả về thông tin user bao gồm 'role' để Android điều hướng
        return response()->json([
            'success' => true,
            'message' => $user->is_first_login ? 'Đăng nhập thành công. Vui lòng đổi mật khẩu mới.' : 'Đăng nhập thành công.',
            'data' => [
                'user' => $user, // Chứa role (admin hoặc tenant)
                'is_first_login' => $user->is_first_login,
                'authorization' => [
                    'access_token' => $token,
                    'type' => 'bearer',
                    'expires_in' => $auth->factory()->getTTL() * 60
                ]
            ]
        ]);
    }

    /**
     * API Bắt buộc đổi mật khẩu ở lần đăng nhập đầu
     */
    public function firstLoginChangePassword(Request $request)
    {
        /** @var \App\Models\User $user */
        $user = auth('api')->user();

        if (!$user->is_first_login) {
            return response()->json(['error' => 'Tài khoản này đã thiết lập mật khẩu trước đó.'], 403);
        }

        $validator = Validator::make($request->all(), [
            'new_password' => 'required|string|min:6|confirmed',
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $user->password = Hash::make($request->new_password);
        $user->is_first_login = false;
        $user->save();

        return response()->json([
            'success' => true,
            'message' => 'Thiết lập mật khẩu mới thành công.'
        ]);
    }

    /**
     * API Refresh Token
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

    public function register(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name' => 'required|string|max:255',
            'phone' => 'required|string|unique:users',
            'password' => 'required|string|min:6|confirmed',
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        $user = User::create([
            'name' => $request->name,
            'phone' => $request->phone,
            'password' => Hash::make($request->password),
            'role' => 'tenant',
            'is_first_login' => false,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Đăng ký tài khoản thành công.',
            'data' => $user
        ], 201);
    }

    public function changePassword(Request $request)
    {
        /** @var \App\Models\User $user */
        $user = auth('api')->user();

        $validator = Validator::make($request->all(), [
            'current_password' => 'required|string',
            'new_password' => 'required|string|min:6|confirmed',
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        if (!Hash::check($request->current_password, $user->password)) {
            return response()->json(['error' => 'Mật khẩu hiện tại không đúng.'], 400);
        }

        $user->password = Hash::make($request->new_password);
        $user->save();

        return response()->json([
            'success' => true,
            'message' => 'Đổi mật khẩu thành công.'
        ]);
    }

   public function logout()
{
    try {
        JWTAuth::invalidate(JWTAuth::getToken());
        return response()->json([
            'success' => true,
            'message' => 'Đăng xuất thành công'
        ]);
    } catch (\Exception $e) {
        return response()->json(['error' => 'Không thể đăng xuất, token có thể đã hết hạn.'], 500);
    }
}
}