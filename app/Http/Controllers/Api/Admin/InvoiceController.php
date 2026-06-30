<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Invoice;
use App\Models\Room;
use App\Models\Service;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class InvoiceController extends Controller
{
    /**
     * API Tạo và Tính toán Hóa đơn tự động
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'room_id'         => 'required|exists:rooms,id',
            'billing_month'   => 'required|string|max:10', // VD: 06/2026
            'electricity_old' => 'required|integer|min:0',
            'electricity_new' => 'required|integer|gte:electricity_old', // Số mới phải >= số cũ
            'water_old'       => 'required|integer|min:0',
            'water_new'       => 'required|integer|gte:water_old',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Dữ liệu không hợp lệ.',
                'errors'  => $validator->errors()
            ], 422);
        }

        // 1. Lấy thông tin Phòng để biết Giá thuê phòng
        $room = Room::find($request->room_id);

        // 2. Lấy giá Dịch vụ từ DB (Tìm theo tên)
        $dien = Service::where('name', 'Tiền Điện')->first();
        $nuoc = Service::where('name', 'Tiền Nước')->first();

        // Nếu không tìm thấy trong DB thì gán giá mặc định
        $giaDien = $dien ? $dien->price : 3500;
        $giaNuoc = $nuoc ? $nuoc->price : 20000;

        // 3. Thực hiện tính toán
        $soDienSD = $request->electricity_new - $request->electricity_old;
        $soNuocSD = $request->water_new - $request->water_old;

        $tienDien = $soDienSD * $giaDien;
        $tienNuoc = $soNuocSD * $giaNuoc;
        
        // Tổng tiền = Giá phòng + Tiền điện + Tiền nước
        $tongTien = $room->price + $tienDien + $tienNuoc;

        // 4. Lưu Hóa đơn vào Database
        $invoice = Invoice::create([
            'room_id'         => $request->room_id,
            'billing_month'   => $request->billing_month,
            'electricity_old' => $request->electricity_old,
            'electricity_new' => $request->electricity_new,
            'water_old'       => $request->water_old,
            'water_new'       => $request->water_new,
            'total_amount'    => $tongTien,
            'status'          => 'unpaid' // Trạng thái: Chưa thanh toán
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Chốt hóa đơn thành công!',
            'data'    => $invoice,
            'details' => [
                'tien_phong' => $room->price,
                'tien_dien'  => $tienDien,
                'tien_nuoc'  => $tienNuoc,
                'tong_cong'  => $tongTien
            ]
        ], 201);
    }
    public function splitBill(Request $request, $id)
    {
        // 1. Tìm hóa đơn
        $invoice = Invoice::find($id);
        if (!$invoice) {
            return response()->json([
                'success' => false,
                'message' => 'Không tìm thấy hóa đơn.'
            ], 404);
        }

        // 2. Validate dữ liệu truyền lên (chờ mảng members_config)
        $validator = Validator::make($request->all(), [
            'members_config'                => 'required|array|min:1',
            'members_config.*.name'         => 'required|string',
            'members_config.*.room_weight'  => 'required|numeric|min:0',
            'members_config.*.elec_weight'  => 'required|numeric|min:0',
            'members_config.*.water_weight' => 'required|numeric|min:0',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'errors'  => $validator->errors()
            ], 422);
        }

        // 3. Lấy lại thông tin tiền phòng, điện, nước từ DB
        $room = $invoice->room;
        $tienPhong = $room->price;
        
        $dien = \App\Models\Service::where('name', 'Tiền Điện')->first();
        $nuoc = \App\Models\Service::where('name', 'Tiền Nước')->first();
        $giaDien = $dien ? $dien->price : 3500;
        $giaNuoc = $nuoc ? $nuoc->price : 20000;

        $tienDien = ($invoice->electricity_new - $invoice->electricity_old) * $giaDien;
        $tienNuoc = ($invoice->water_new - $invoice->water_old) * $giaNuoc;

        // 4. Thuật toán tính theo Hệ số (Weight)
        $configs = $request->members_config;
        
        $totalRoomWeight = 0;
        $totalElecWeight = 0;
        $totalWaterWeight = 0;

        // Cộng dồn để xem chia làm mấy phần
        foreach ($configs as $config) {
            $totalRoomWeight += $config['room_weight'];
            $totalElecWeight += $config['elec_weight'];
            $totalWaterWeight += $config['water_weight'];
        }

        if ($totalRoomWeight == 0) {
            return response()->json(['success' => false, 'message' => 'Tổng hệ số phòng phải lớn hơn 0'], 400);
        }

        // Tính giá trị của 1 "phần" (unit)
        $unitRoomPrice = $tienPhong / $totalRoomWeight;
        $unitElecPrice = $totalElecWeight > 0 ? ($tienDien / $totalElecWeight) : 0;
        $unitWaterPrice = $totalWaterWeight > 0 ? ($tienNuoc / $totalWaterWeight) : 0;

        // 5. Xuất báo cáo chi tiết cho từng người
        $breakdown = [];
        foreach ($configs as $config) {
            $myRoomCost = $config['room_weight'] * $unitRoomPrice;
            $myElecCost = $config['elec_weight'] * $unitElecPrice;
            $myWaterCost = $config['water_weight'] * $unitWaterPrice;
            
            $totalCost = $myRoomCost + $myElecCost + $myWaterCost;

            $breakdown[] = [
                'name'           => $config['name'],
                'tien_phong'     => round($myRoomCost),
                'tien_dien'      => round($myElecCost),
                'tien_nuoc'      => round($myWaterCost),
                'tong_phai_dong' => round($totalCost)
            ];
        }

        return response()->json([
            'success'       => true,
            'message'       => 'Chia tiền theo hệ số hoàn tất!',
            'invoice_total' => $invoice->total_amount,
            'breakdown'     => $breakdown
        ], 200);
    }
}