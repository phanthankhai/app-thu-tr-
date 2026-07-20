<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Bill;
use App\Models\Room;
use App\Models\User;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\DB;

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
            'utility_payer_ids' => 'nullable|array',
            'utility_payer_ids.*' => 'exists:users,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        // Lấy phòng kèm danh sách dịch vụ
        $room = Room::with('services')->find($request->room_id);

        if (!$room) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy thông tin phòng trọ.'], 404);
        }

        // --- TÍNH TIỀN DỊCH VỤ CHÍNH XÁC ---
        $servicesCost = 0;
        foreach ($room->services as $service) {
            $servicesCost += (float)$service->price; 
        }

        // Tính tiền điện nước
        $electricCost = ($request->new_electric - $request->old_electric) * 3500;
        $waterCost = ($request->new_water - $request->old_water) * 15000;
        
        // Tổng tiền = Phòng + Điện + Nước + Dịch vụ
        $totalAmount = (float)$room->price + $electricCost + $waterCost + $servicesCost;

        // --- LƯU HÓA ĐƠN VỚI CÁC GIÁ TRỊ ĐÃ TÍNH ---
        $bill = Bill::create([
            'room_id'        => $request->room_id,
            'billing_month'  => $request->billing_month,
            'old_electric'   => $request->old_electric,
            'new_electric'   => $request->new_electric,
            'electric_price' => 3500,
            'old_water'      => $request->old_water,
            'new_water'      => $request->new_water,
            'water_price'    => 15000,
            'room_price'     => $room->price,
            'services_cost'  => $servicesCost, // Giá trị dịch vụ đã lấy được từ log
            'total_amount'   => $totalAmount,  // Tổng tiền đã cộng dịch vụ
            'status'         => 'unpaid'
        ]);

        // Phân bổ chi phí cho các thành viên
        $tenants = User::where('room_id', $request->room_id)->where('role', 'tenant')->get();
        $tenantCount = $tenants->count();
        $splitDetails = [];

        if ($tenantCount > 0) {
            $utilityPayers = $request->utility_payer_ids ?? $tenants->pluck('id')->toArray();
            $utilityPayerCount = count($utilityPayers);

            $utilityPerPerson = $utilityPayerCount > 0 ? (($electricCost + $waterCost) / $utilityPayerCount) : 0;
            $rentAndServicePerPerson = ($room->price + $servicesCost) / $tenantCount;

            foreach ($tenants as $tenant) {
                $tenantTotal = $rentAndServicePerPerson;
                if (in_array($tenant->id, $utilityPayers)) {
                    $tenantTotal += $utilityPerPerson;
                }
                $splitDetails[$tenant->name] = round($tenantTotal);
            }
        }

        return response()->json([
            'success' => true,
            'message' => 'Lập hóa đơn thành công.',
            'data'    => [
                'bill_info'     => $bill,
                'services_cost' => $servicesCost,
                'total_bill'    => $totalAmount,
                'split_details' => $splitDetails
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
    $bill = \App\Models\Bill::find($id);

    if (!$bill) {
        return response()->json(['success' => false, 'message' => 'Không tìm thấy hóa đơn.'], 404);
    }

    $validator = \Illuminate\Support\Facades\Validator::make($request->all(), [
        'payment_method' => 'required|in:cash,transfer', 
        'amount'         => 'required|numeric|min:0' 
    ]);

    if ($validator->fails()) {
        return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
    }

    if ($request->payment_method === 'transfer') {
        return response()->json([
            'success' => false, 
            'message' => 'Tính năng thanh toán chuyển khoản đang trong giai đoạn nghiên cứu, vui lòng chọn tiền mặt!'
        ], 400);
    }

    $user = auth('api')->user();

    // Đồng bộ chữ 'transfer' từ điện thoại thành 'bank_transfer' của Database
    $dbMethod = $request->payment_method === 'transfer' ? 'bank_transfer' : 'cash';

    try {
        // Ghi lịch sử thanh toán lẻ vào bảng payments
        \App\Models\Payment::create([
            'bill_id'        => $bill->id,
            'user_id'        => $user->id,
            'amount'         => $request->amount,
            'payment_method' => $dbMethod,
            'status'         => 'pending' 
        ]);

        // ĐÃ XÓA KHỐI $bill->update TẠI ĐÂY ĐỂ TRÁNH HOÀN TOÀN LỖI ENUM (Data truncated)

    } catch (\Exception $e) {
        \Illuminate\Support\Facades\Log::error("Lỗi lưu Payment lẻ: " . $e->getMessage());
        return response()->json([
            'success' => false,
            'message' => 'Hệ thống gặp lỗi khi tạo lịch sử giao dịch. Chi tiết: ' . $e->getMessage()
        ], 500);
    }

    $formattedAmount = number_format($request->amount, 0, ',', '.');

    return response()->json([
        'success' => true,
        'message' => "Đã gửi thông báo thanh toán phần tiền của bạn ({$formattedAmount}đ). Vui lòng đợi Admin xác nhận."
    ], 200);
}
public function getPendingPayments()
{
    if (auth('api')->user()->role !== 'admin') {
        return response()->json(['success' => false, 'message' => 'Bạn không có quyền.'], 403);
    }

    // Lấy các giao dịch đang 'pending', kèm thông tin người đóng, thông tin hóa đơn và phòng
    $payments = \App\Models\Payment::where('status', 'pending')
                    ->with(['user:id,name,phone', 'bill.room:id,name,price'])
                    ->orderBy('created_at', 'desc')
                    ->get();

    return response()->json([
        'success' => true,
        'message' => 'Lấy danh sách chờ duyệt thành công.',
        'data'    => $payments
    ], 200);
}

// 2. API Admin bấm xác nhận đã nhận tiền thành công
public function approvePayment($id)
{
    if (auth('api')->user()->role !== 'admin') {
        return response()->json(['success' => false, 'message' => 'Bạn không có quyền.'], 403);
    }

    $payment = \App\Models\Payment::find($id);

    if (!$payment) {
        return response()->json(['success' => false, 'message' => 'Không tìm thấy giao dịch lẻ này.'], 404);
    }

    if ($payment->status === 'approved') {
        return response()->json(['success' => false, 'message' => 'Giao dịch này đã được duyệt trước đó.'], 400);
    }

    try {
        DB::beginTransaction();

        // Bước A: Cập nhật biên lai lẻ này thành 'approved' (Đã nhận tiền)
        $payment->update(['status' => 'approved']);

        // Bước B: Tính toán tổng tiền hóa đơn gốc để làm mốc so sánh
        $bill = \App\Models\Bill::find($payment->bill_id);
        if ($bill) {
            // Thực hiện tính toán tổng tiền y hệt như công thức dưới điện thoại của khách
            $tienDien = (($bill->new_electric ?? 0) - ($bill->old_electric ?? 0)) * 3500;
            $tienNuoc = (($bill->new_water ?? 0) - ($bill->old_water ?? 0)) * 15000;
            $roomPrice = $bill->room ? $bill->room->price : 0;
            
            $totalBillAmount = $roomPrice + $tienDien + $tienNuoc;

            // Bước C: Tính tổng toàn bộ tiền lẻ ĐÃ DUYỆT THÀNH CÔNG của hóa đơn này từ trước tới nay
            $totalApprovedAmount = \App\Models\Payment::where('bill_id', $bill->id)
                                                      ->where('status', 'approved')
                                                      ->sum('amount');

            // Bước D: Thuật toán thông minh tự động chốt sổ hóa đơn tổng
            // Nếu tổng số tiền các bạn trong phòng góp lại đã ĐỦ HOẶC VƯỢT tổng hóa đơn phòng
            if ($totalApprovedAmount >= $totalBillAmount) {
                $bill->update(['status' => 'paid']); // Chuyển trạng thái hóa đơn gốc thành PAID (Đã thanh toán)
            }
        }

        DB::commit();

        return response()->json([
            'success' => true,
            'message' => 'Đã duyệt thành công khoản tiền lẻ này!'
        ], 200);

    } catch (\Exception $e) {
        DB::rollBack();
        \Illuminate\Support\Facades\Log::error("Lỗi duyệt payment: " . $e->getMessage());
        return response()->json(['success' => false, 'message' => 'Lỗi hệ thống khi duyệt tiền.'], 500);
    }
}
}