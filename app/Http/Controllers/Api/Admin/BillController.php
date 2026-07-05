<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Bill;
use App\Models\Room;
use App\Models\User;
use Illuminate\Support\Facades\Validator;

class BillController extends Controller
{
    // 1. Lấy danh sách toàn bộ hóa đơn của khu trọ
    public function index()
    {
        $bills = Bill::with('room.services')->orderBy('created_at', 'desc')->get();
        return response()->json([
            'success' => true,
            'message' => 'Lấy danh sách hóa đơn toàn khu trọ thành công.',
            'data'    => $bills
        ], 200);
    }

    // 2. Chốt số điện nước và lập hóa đơn tháng tự động
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'room_id'           => 'required|exists:rooms,id',
            'billing_month'     => 'required|string',
            'old_electric'      => 'required|integer|min:0',
            'new_electric'      => 'required|integer|gte:old_electric',
            'old_water'         => 'required|integer|min:0',
            'new_water'         => 'required|integer|gte:old_water',
            'utility_payer_ids' => 'nullable|array', // Mảng ID của những người đóng tiền điện nước
            'utility_payer_ids.*' => 'exists:users,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        $room = Room::with('services')->find($request->room_id);

        if (!$room) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy thông tin phòng trọ.'], 404);
        }

        // Ghi nhận và lưu trữ thông tin hóa đơn gốc
        $bill = Bill::create([
            'room_id'       => $request->room_id,
            'billing_month' => $request->billing_month,
            'old_electric'  => $request->old_electric,
            'new_electric'  => $request->new_electric,
            'electric_price'=> 3500,
            'old_water'     => $request->old_water,
            'new_water'     => $request->new_water,
            'water_price'   => 15000,
            'room_price'    => $room->price,
            'status'        => 'unpaid'
        ]);

        // Tính toán các khoản chi phí chi tiết
        $electricCost = ($request->new_electric - $request->old_electric) * 3500;
        $waterCost = ($request->new_water - $request->old_water) * 15000;
        $totalUtilities = $electricCost + $waterCost;
        
        $servicesCost = $bill->services_cost; 
        $totalBill = $bill->total_amount;

        // Tối ưu thuật toán quét và bóc tách chi phí
        $tenants = User::where('room_id', $request->room_id)->where('role', 'tenant')->get();
        $tenantCount = $tenants->count();
        $splitDetails = [];

        if ($tenantCount > 0) {
            // Lấy danh sách người đóng điện nước (Nếu App không gửi, mặc định chia đều cho tất cả)
            $utilityPayers = $request->utility_payer_ids ?? $tenants->pluck('id')->toArray();
            $utilityPayerCount = count($utilityPayers);

            // Tiền điện nước chỉ chia cho những người nằm trong danh sách chỉ định
            $utilityPerPerson = $utilityPayerCount > 0 ? ($totalUtilities / $utilityPayerCount) : 0;
            
            // Tiền phòng cố định thì chia đều cho tất cả mọi người có mặt trong phòng
            $rentPerPerson = $room->price / $tenantCount; 

            foreach ($tenants as $tenant) {
                $tenantTotal = $rentPerPerson;

                // Kiểm tra xem người này có phải gánh tiền điện nước không
                if (in_array($tenant->id, $utilityPayers)) {
                    $tenantTotal += $utilityPerPerson;
                }

                $splitDetails[$tenant->name] = round($tenantTotal);
            }
        } else {
            $splitDetails = ["Notice" => "Phòng trống hoặc chưa có khách thuê kích hoạt."];
        }

        return response()->json([
            'success' => true,
            'message' => 'Lập hóa đơn và phân bổ chi phí thành công.',
            'data'    => [
                'bill_info'      => $bill,
                'electric_cost'  => $electricCost,
                'water_cost'     => $waterCost,
                'services_cost'  => $servicesCost,
                'total_bill'     => $totalBill,
                'split_details'  => $splitDetails
            ]
        ], 201);
    }

    // 3. Xác nhận thanh toán
    public function markAsPaid($id)
    {
        $bill = Bill::find($id);

        if (!$bill) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy hóa đơn.'], 404);
        }

        if ($bill->status === 'paid') {
            return response()->json(['success' => false, 'message' => 'Hóa đơn này đã được thanh toán trước đó!'], 400);
        }

        $bill->update(['status' => 'paid']);

    return response()->json([
        'success' => true,
        'message' => 'Xác nhận thanh toán thành công!',
        'data'    => $bill
    ], 200);
    }

    public function tenantNotifyPayment(Request $request, $id)
{
    $bill = Bill::find($id);

    if (!$bill) {
        return response()->json(['success' => false, 'message' => 'Không tìm thấy hóa đơn.'], 404);
    }

    $validator = Validator::make($request->all(), [
        'payment_method' => 'required|in:cash,transfer', // Chỉ chấp nhận tiền mặt hoặc chuyển khoản
    ]);

    if ($validator->fails()) {
        return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
    }

    // Chặn nếu chọn chuyển khoản vì tính năng này đang nghiên cứu
    if ($request->payment_method === 'transfer') {
        return response()->json([
            'success' => false, 
            'message' => 'Tính năng thanh toán chuyển khoản đang trong giai đoạn nghiên cứu, vui lòng chọn tiền mặt!'
        ], 400);
    }

    // Chuyển trạng thái hóa đơn từ unpaid -> pending (Chờ xác nhận)
    $bill->update([
        'status' => 'pending'
    ]);

    return response()->json([
        'success' => true,
        'message' => 'Đã gửi yêu cầu thanh toán bằng tiền mặt. Vui lòng đợi Admin xác nhận.'
    ], 200);
}
}