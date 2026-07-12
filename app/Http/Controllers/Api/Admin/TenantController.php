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
    public function getMyDashboard()
    {
        // 1. Lấy thông tin khách thuê đang đăng nhập
        $user = auth('api')->user(); 

        if (!$user->room_id) {
            return response()->json([
                'success' => false,
                'message' => 'Tài khoản của bạn chưa được xếp vào phòng nào.'
            ], 400);
        }

        // 2. Lấy thông tin phòng, danh sách thành viên VÀ hợp đồng đang hiệu lực
        $room = \App\Models\Room::with([
            'users' => function($query) {
                $query->where('role', 'tenant'); 
            },
            'contracts' => function($query) {
                // Chỉ lấy hợp đồng đang active hoặc pending để check thanh lý
                $query->whereNull('end_date')
                      ->orWhere('status', 'pending_termination');
            }
        ])->find($user->room_id);

        // 3. Lấy hóa đơn hiện tại của phòng
        $bill = \App\Models\Bill::where('room_id', $user->room_id)
                                ->whereIn('status', ['unpaid', 'pending'])
                                ->first();

        if ($bill) {
            $payment = \App\Models\Payment::where('bill_id', $bill->id)
                                          ->where('user_id', $user->id)
                                          ->first();
            $bill->my_payment_status = $payment ? $payment->status : 'not_paid_yet';
        }

        return response()->json([
            'success' => true,
            'message' => 'Lấy dữ liệu Dashboard thành công.',
            'data'    => [
                'my_info' => $user,
                'room'    => $room, 
                'bill'    => $bill
            ]
        ], 200);
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
            'phone'   => 'required|string|regex:/^([0-9\s\-\+\(\)]*)$/|min:10|unique:users,phone', 
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        $room = Room::find($request->room_id);

        // 3. Kiểm tra xem phòng đã đầy chưa
        $currentTenantsCount = User::where('room_id', $room->id)->where('role', 'tenant')->count();
        $maxOccupants = $room->max_occupants ?? 4; 
        
        if ($currentTenantsCount >= $maxOccupants) {
            return response()->json([
                'success' => false,
                'message' => "Phòng {$room->name} đã đạt số người ở tối đa ({$maxOccupants}). Không thể thêm."
            ], 400);
        }

        // 4. Khởi tạo tài khoản người ở ghép với mật khẩu 6 số ngẫu nhiên
        $temporaryPassword = (string) rand(100000, 999999);

        $user = User::create([
            'name'           => $request->name,
            'phone'          => $request->phone,
            'room_id'        => $request->room_id, 
            'role'           => 'tenant',
            'password'       => Hash::make($temporaryPassword),
            'is_first_login' => true,
        ]);

        // 5. Gửi SMS mật khẩu tạm (Ghi log hệ thống)
        Log::info('--- THÔNG TIN TÀI KHOẢN Ở GHÉP MỚI ---');
Log::info('SĐT: ' . $user->phone);
Log::info('Mật khẩu tạm thời: ' . $temporaryPassword);
Log::info('-----------------------------------------');

        return response()->json([
            'success' => true,
            'message' => 'Đã thêm người ở ghép và tạo tài khoản thành công.',
            'data'    => [
                'tenant'   => $user,
                'temp_pwd' => $temporaryPassword 
            ]
        ], 201);
    }
}