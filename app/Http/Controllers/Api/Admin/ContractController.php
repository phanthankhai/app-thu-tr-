<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Contract;
use App\Models\Room;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class ContractController extends Controller
{
    /**
     * API Lấy danh sách hợp đồng
     */
    public function index()
    {
        // Lấy danh sách hợp đồng, kèm theo thông tin của căn phòng đó (with 'room')
        $contracts = Contract::with('room')->orderBy('created_at', 'desc')->get();

        return response()->json([
            'success' => true,
            'message' => 'Lấy danh sách hợp đồng thành công.',
            'data'    => $contracts
        ], 200);
    }

    /**
     * API Tạo hợp đồng thuê phòng mới
     */
    public function store(Request $request)
    {
        // 1. Kiểm tra dữ liệu đầu vào
        $validator = Validator::make($request->all(), [
            'room_id'      => 'required|exists:rooms,id', // Phải là ID phòng có thật trong DB
            'tenant_name'  => 'required|string|max:255',
            'tenant_phone' => 'required|string|max:20',
            'deposit'      => 'nullable|numeric|min:0',
            'start_date'   => 'required|date',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Dữ liệu không hợp lệ.',
                'errors'  => $validator->errors()
            ], 422);
        }

        // 2. Kiểm tra xem phòng có đang trống không
        $room = Room::find($request->room_id);
        if ($room->status !== 'empty') {
            return response()->json([
                'success' => false,
                'message' => 'Phòng này đã có người thuê hoặc đang bảo trì, không thể lập hợp đồng.'
            ], 400);
        }

        // 3. Tạo hợp đồng mới
        $contract = Contract::create([
            'room_id'      => $request->room_id,
            'tenant_name'  => $request->tenant_name,
            'tenant_phone' => $request->tenant_phone,
            'deposit'      => $request->deposit ?? 0,
            'start_date'   => $request->start_date,
            'status'       => 'active'
        ]);

        // 4. Đổi trạng thái phòng thành "Đã thuê" (occupied)
        $room->update(['status' => 'occupied']);

        return response()->json([
            'success' => true,
            'message' => 'Lập hợp đồng thành công. Phòng đã chuyển sang trạng thái đã thuê.',
            'data'    => $contract
        ], 201);
    }
}