<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class CheckRole
{
    /**
     * Handle an incoming request.
     *
     * @param  Closure(Request): (Response)  $next
     */
    public function handle(Request $request, Closure $next, $role)
{
    // Kiểm tra xem user đã đăng nhập chưa
    if (!auth('api')->check()) {
        return response()->json(['message' => 'Bạn chưa đăng nhập.'], 401);
    }

    // Kiểm tra vai trò
    if (auth('api')->user()->role !== $role) {
        return response()->json(['message' => 'Bạn không có quyền truy cập.'], 403);
    }

    return $next($request);
}
}
