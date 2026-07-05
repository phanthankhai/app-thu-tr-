<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\User;
use App\Models\Room;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Log;

class TenantController extends Controller
{
    // 1. GET /api/v1/admin/tenants -> Lấy danh sách toàn bộ khách thuê
    public function index()
    {
        // Lấy những user có role là tenant, kèm theo thông tin phòng họ đang ở
        $tenants = User::where('role', 'tenant')
                       ->with('room')
                       ->orderBy('created_at', 'desc')
                       ->get();

        return response()->json([
            'success' => true,
            'message' => 'Lấy danh sách khách thuê thành công.',
            'data'    => $tenants
        ], 200);
    }

    // 2. GET /api/v1/admin/tenants/{id} -> Xem chi tiết hồ sơ 1 khách thuê
    public function show($id)
    {
        // Lấy chi tiết khách thuê, kèm phòng và danh sách hóa đơn chưa thanh toán của phòng đó
        $tenant = User::where('role', 'tenant')
                       ->with(['room.bills' => function($query) {
                           $query->where('status', 'unpaid');
                       }])
                       ->find($id);

        if (!$tenant) {
            return response()->json([
                'success' => false,
                'message' => 'Không tìm thấy thông tin khách thuê này.'
            ], 404);
        }

        return response()->json([
            'success' => true,
            'message' => 'Lấy chi tiết hồ sơ khách thuê thành công.',
            'data'    => $tenant
        ], 200);
    }

    // 3. PUT /api/v1/admin/tenants/{id} -> Cập nhật thông tin khách thuê
    public function update(Request $request, $id)
    {
        $tenant = User::where('role', 'tenant')->find($id);

        if (!$tenant) {
            return response()->json(['success' => false, 'message' => 'Khách thuê không tồn tại.'], 404);
        }

        $validator = Validator::make($request->all(), [
            'name'  => 'required|string|max:255',
            'phone' => 'required|string|unique:users,phone,' . $id, // Bỏ qua kiểm tra trùng với chính nó
            'email' => 'nullable|email|unique:users,email,' . $id,
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        $tenant->update($request->only(['name', 'phone', 'email']));

        return response()->json([
            'success' => true,
            'message' => 'Cập nhật thông tin khách thuê thành công.',
            'data'    => $tenant
        ], 200);
    }

    // 4. POST /api/v1/admin/tenants/{id}/end-rent -> Kết thúc hợp đồng (Đá khách khỏi phòng)
    public function endRent($id)
    {
        $tenant = User::where('role', 'tenant')->find($id);

        if (!$tenant) {
            return response()->json(['success' => false, 'message' => 'Khách thuê không tồn tại.'], 404);
        }

        $roomId = $tenant->room_id;
        if (!$roomId) {
            return response()->json(['success' => false, 'message' => 'Khách thuê này hiện tại đang không ở phòng nào.'], 400);
        }

        // Bỏ liên kết phòng (Set room_id về null)
        $tenant->room_id = null;
        $tenant->save();

        // LOGIC THÔNG MINH: Kiểm tra xem phòng cũ còn ai ở không
        $remainingTenants = User::where('room_id', $roomId)->where('role', 'tenant')->count();
        
        // Nếu không còn ai ở phòng này nữa, tự động cập nhật trạng thái phòng về 'empty' (Trống)
        if ($remainingTenants === 0) {
            $room = Room::find($roomId);
            if ($room) {
                $room->update(['status' => 'empty']);
            }
        }

        return response()->json([
            'success' => true,
            'message' => 'Đã chấm dứt hợp đồng thuê phòng thành công và cập nhật trạng thái phòng.',
            'data'    => $tenant
        ], 200);
    }

    /**
     * API Dashboard cá nhân của Khách thuê
     */
    public function getMyDashboard(Request $request)
    {
        if (auth('api')->user()->role !== 'tenant') {
        return response()->json(['message' => 'Bạn không có quyền truy cập.'], 403);
    }
        /** @var \App\Models\User $user */
        $user = auth('api')->user();

        // Kiểm tra nếu người dùng chưa có phòng
        if (!$user->room_id) {
            return response()->json([
                'success' => false,
                'message' => 'Bạn chưa được xếp phòng.'
            ], 404);
        }

        // Lấy thông tin phòng của user cùng với các hóa đơn chưa thanh toán
        $room = $user->room()->with(['bills' => function($query) {
            $query->where('status', 'unpaid'); // Chỉ lấy hóa đơn chưa trả
        }])->first();

        return response()->json([
            'success' => true,
            'data' => [
                'user' => $user->only(['name', 'phone']),
                'room' => $room,
                'current_bill' => $room->bills->first() ?? null // Hóa đơn gần nhất cần thanh toán
            ]
        ]);
    }

    public function addCoTenant(Request $request)
    {
        // 1. Chỉ Admin mới được thêm người
        if (auth('api')->user()->role !== 'admin') {
            return response()->json(['success' => false, 'message' => 'Bạn không có quyền thực hiện hành động này.'], 403);
        }

        // 2. Kiểm tra dữ liệu đầu vào
        $validator = Validator::make($request->all(), [
            'room_id' => 'required|exists:rooms,id',
            'name'    => 'required|string|max:255',
            'phone'   => 'required|string|regex:/^([0-9\s\-\+\(\)]*)$/|min:10|unique:users,phone', // Đảm bảo SĐT chưa có tài khoản
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        $room = Room::find($request->room_id);

        // 3. (Tùy chọn) Kiểm tra xem phòng đã đầy chưa
        $currentTenantsCount = User::where('room_id', $room->id)->where('role', 'tenant')->count();
        $maxOccupants = $room->max_occupants ?? 4; // Giả sử giới hạn là 4 nếu chưa có cột trong DB
        
        if ($currentTenantsCount >= $maxOccupants) {
            return response()->json([
                'success' => false,
                'message' => "Phòng {$room->name} đã đạt số người ở tối đa ({$maxOccupants}). Không thể thêm."
            ], 400);
        }

        // 4. Khởi tạo tài khoản người ở ghép
        $temporaryPassword = (string) rand(100000, 999999);

        $user = User::create([
            'name'           => $request->name,
            'phone'          => $request->phone,
            'room_id'        => $request->room_id, // Nhét người này vào chung phòng
            'role'           => 'tenant',
            'password'       => Hash::make($temporaryPassword),
            'is_first_login' => true,
        ]);

        // 5. Gửi SMS mật khẩu tạm (Giả lập)
        Log::info("SMS gửi tới SĐT [{$user->phone}]: Tai khoan o ghep tai SmartRent cua ban da duoc tao. Mat khau: {$temporaryPassword}");

        return response()->json([
            'success' => true,
            'message' => 'Đã thêm người ở ghép và tạo tài khoản thành công.',
            'data'    => [
                'tenant'   => $user,
                'temp_pwd' => $temporaryPassword // Trả về để Admin biết nếu cần
            ]
        ], 201);
    }
}