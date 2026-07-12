<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Contract;
use App\Models\Room;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

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

        // 2. Kiểm tra phòng trống
        $room = Room::find($request->room_id);
        if ($room->status !== 'empty') {
            return response()->json([
                'success' => false,
                'message' => 'Phòng này đã có người thuê hoặc đang bảo trì, không thể lập hợp đồng.'
            ], 400);
        }

        DB::beginTransaction();

        try {
            // 3. Xử lý tài khoản User
            $user = User::where('phone', $request->tenant_phone)->first();
            
            if (!$user) {
                // Sinh mật khẩu tạm ngẫu nhiên 6 số để dễ quản lý
                $tempPassword = (string) rand(100000, 999999);

                // Ghi log mật khẩu để Admin debug khi cần
                Log::info('--- HỢP ĐỒNG MỚI: TẠO TÀI KHOẢN KHÁCH THUÊ ---');
                Log::info('SĐT khách: ' . $request->tenant_phone);
                Log::info('Mật khẩu tạm thời: ' . $tempPassword);
                Log::info('--------------------------------------------');

                $user = User::create([
                    'name'           => $request->tenant_name,
                    'phone'          => $request->tenant_phone,
                    'room_id'        => $request->room_id,
                    'role'           => 'tenant',
                    'password'       => Hash::make($tempPassword), 
                    'is_first_login' => true,
                ]);
            } else {
                $user->update(['room_id' => $request->room_id]);
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

            // 5. Đổi trạng thái phòng thành "Đã thuê"
            $room->update(['status' => 'occupied']);

            DB::commit();

            return response()->json([
                'success' => true,
                'message' => 'Lập hợp đồng thành công. Tài khoản khách thuê đã được tạo/đồng bộ.',
                'data'    => $contract
            ], 201);

        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Lỗi lập hợp đồng: ' . $e->getMessage());
            return response()->json([
                'success' => false,
                'message' => 'Đã xảy ra lỗi hệ thống khi lập hợp đồng.',
                'error'   => $e->getMessage()
            ], 500);
        }
    }

    /**
     * API 1: YÊU CẦU thanh lý hợp đồng (Bắt đầu quy trình)
     */
    public function requestTermination(Request $request, $id)
    {
        $contract = Contract::find($id);

        if (!$contract) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy hợp đồng.'], 404);
        }

        if ($contract->status !== 'active') {
            return response()->json(['success' => false, 'message' => 'Hợp đồng không ở trạng thái đang hoạt động.'], 400);
        }

        $user = auth('api')->user();

        // Đánh dấu bên nào chủ động gửi yêu cầu
        if ($user->role === 'admin') {
            $contract->admin_approved = true;
        } else {
            $contract->tenant_approved = true;
        }
        
        $contract->status = 'pending_termination';
        $contract->save();

        return response()->json([
            'success' => true, 
            'message' => 'Đã gửi yêu cầu thanh lý. Đang chờ bên còn lại xác nhận.',
            'data'    => $contract
        ], 200);
    }

    /**
     * API 2: XÁC NHẬN thanh lý hợp đồng (Chốt kết thúc)
     */
    public function confirmTermination($id)
    {
        $contract = Contract::find($id);

        if (!$contract) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy hợp đồng.'], 404);
        }

        if ($contract->status !== 'pending_termination') {
            return response()->json(['success' => false, 'message' => 'Hợp đồng này chưa có yêu cầu thanh lý.'], 400);
        }

        $user = auth('api')->user();

        // Đánh dấu bên còn lại xác nhận
        if ($user->role === 'admin') {
            $contract->admin_approved = true;
        } else {
            $contract->tenant_approved = true;
        }

        // Kiểm tra xem đã đủ 2 bên đồng thuận chưa
        if (!$contract->admin_approved || !$contract->tenant_approved) {
            $contract->save();
            return response()->json(['success' => false, 'message' => 'Vẫn đang chờ bên kia xác nhận.'], 400);
        }

        DB::beginTransaction();
        try {
            // 1. Đổi trạng thái hợp đồng thành "Đã thanh lý" (terminated)
            $contract->update(['status' => 'terminated']);

            // 2. Trả lại trạng thái phòng thành "Trống"
            $room = Room::find($contract->room_id);
            if ($room) {
                $room->update(['status' => 'empty']);
            }

            // 3. Gỡ phòng ra khỏi TẤT CẢ User đang ở phòng này
            User::where('room_id', $contract->room_id)->update(['room_id' => null]);

            DB::commit();
            return response()->json([
                'success' => true,
                'message' => 'Cả 2 bên đã xác nhận. Hợp đồng đã thanh lý thành công!',
                'deposit_to_refund' => $contract->deposit // Báo số tiền cọc cần hoàn
            ], 200);

        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Lỗi xác nhận thanh lý: ' . $e->getMessage());
            return response()->json(['success' => false, 'message' => 'Lỗi hệ thống.'], 500);
        }
    }
}