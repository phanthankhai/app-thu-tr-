<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Contract;
use App\Models\Room;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\DB; // Bắt buộc thêm dòng này để dùng Transaction

class ContractController extends Controller
{
    /**
     * API Lấy danh sách hợp đồng
     */
    public function index()
    {
        $contracts = Contract::with('room')->orderBy('created_at', 'desc')->get();

        return response()->json([
            'success' => true,
            'message' => 'Lấy danh sách hợp đồng thành công.',
            'data'    => $contracts
        ], 200);
    }

    /**
     * API Tạo hợp đồng thuê phòng mới và tự động xử lý tài khoản
     */
    public function store(Request $request)
    {
        // 1. Kiểm tra dữ liệu đầu vào
        $validator = Validator::make($request->all(), [
            'room_id'      => 'required|exists:rooms,id', 
            'tenant_name'  => 'required|string|max:255',
            'tenant_phone' => 'required|string|max:20',
            'tenant_cccd'  => 'required|string|max:20', 
            'start_date'   => 'required|date',
            'end_date'     => 'nullable|date|after_or_equal:start_date', 
            'deposit'      => 'nullable|numeric|min:0',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Dữ liệu không hợp lệ.',
                'errors'  => $validator->errors()
            ], 422);
        }

        // 2. KIỂM TRA PHÒNG TRỐNG TRƯỚC (Phải đặt lên trên đầu)
        $room = Room::find($request->room_id);
        if ($room->status !== 'empty') {
            return response()->json([
                'success' => false,
                'message' => 'Phòng này đã có người thuê hoặc đang bảo trì, không thể lập hợp đồng.'
            ], 400);
        }

        // Kích hoạt Transaction: Nếu một trong các bước dưới lỗi, toàn bộ sẽ hủy bỏ, không lo rác DB
        DB::beginTransaction();

        try {
            // 3. XỬ LÝ TÀI KHOẢN USER
            $user = User::where('phone', $request->tenant_phone)->first();
            
            if (!$user) {
                // Nếu SĐT này chưa từng đăng ký -> Tạo tài khoản mới
                $user = User::create([
                    'name'           => $request->tenant_name,
                    'phone'          => $request->tenant_phone,
                    'room_id'        => $request->room_id,
                    'role'           => 'tenant',
                    'password'       => Hash::make('123456'), 
                    'is_first_login' => true,
                ]);
            } else {
                // NẾU SĐT ĐÃ TỒN TẠI: Cập nhật room_id mới cho họ để đồng bộ với hợp đồng
                $user->update([
                    'room_id' => $request->room_id
                ]);
            }

            // 4. Tạo hợp đồng mới 
            $contract = Contract::create([
                'room_id'      => $request->room_id,
                'tenant_name'  => $request->tenant_name,
                'tenant_phone' => $request->tenant_phone,
                'tenant_cccd'  => $request->tenant_cccd, 
                'start_date'   => $request->start_date,
                'end_date'     => $request->end_date,    
                'deposit'      => $request->deposit ?? 0,
                'status'       => 'active'
            ]);

            // 5. Đổi trạng thái phòng thành "Đã thuê" (occupied)
            $room->update(['status' => 'occupied']);

            // Xác nhận hoàn tất lưu vào DB
            DB::commit();

            return response()->json([
                'success' => true,
                'message' => 'Lập hợp đồng thành công. Tài khoản khách thuê đã được đồng bộ.',
                'data'    => $contract
            ], 201);

        } catch (\Exception $e) {
            // Nếu có bất kỳ lỗi khuất tất nào xảy ra (ví dụ nghẽn mạng, lỗi SQL...), hủy bỏ toàn bộ dữ liệu dự phòng
            DB::rollBack();
            return response()->json([
                'success' => false,
                'message' => 'Đã xảy ra lỗi hệ thống khi lập hợp đồng.',
                'error'   => $e->getMessage()
            ], 500);
        }
    }

    /**
     * API Thanh lý / Kết thúc hợp đồng
     */
    public function terminate($id)
    {
        $contract = Contract::find($id);

        if (!$contract) {
            return response()->json([
                'success' => false,
                'message' => 'Không tìm thấy hợp đồng.'
            ], 404);
        }

        if ($contract->status !== 'active') {
            return response()->json([
                'success' => false,
                'message' => 'Hợp đồng này đã kết thúc hoặc bị hủy từ trước.'
            ], 400);
        }

        DB::beginTransaction();
        try {
            // 1. Đổi trạng thái hợp đồng thành "Đã hết hạn"
            $contract->update(['status' => 'expired']);

            // 2. Trả lại trạng thái phòng thành "Trống"
            $room = Room::find($contract->room_id);
            if ($room) {
                $room->update(['status' => 'empty']);
            }

            // 3. Gỡ phòng ra khỏi tài khoản User thuê phòng này để họ trở thành trạng thái tự do
            User::where('room_id', $contract->room_id)->update(['room_id' => null]);

            DB::commit();
            return response()->json([
                'success' => true,
                'message' => 'Đã kết thúc hợp đồng thành công. Phòng hiện tại đã trống.',
                'data'    => $contract
            ], 200);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json(['success' => false, 'message' => 'Lỗi hệ thống.'], 500);
        }
    }
}