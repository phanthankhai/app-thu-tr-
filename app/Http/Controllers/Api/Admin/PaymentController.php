<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Invoice;
use App\Models\Payment;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class PaymentController extends Controller
{
    /**
     * API Ghi nhận một lần thanh toán cho Hóa đơn
     */
    public function store(Request $request, $invoice_id)
    {
        $invoice = Invoice::find($invoice_id);
        
        if (!$invoice) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy hóa đơn.'], 404);
        }

        if ($invoice->status === 'paid') {
            return response()->json(['success' => false, 'message' => 'Hóa đơn này đã được thanh toán đầy đủ trước đó.'], 400);
        }

        $validator = Validator::make($request->all(), [
            'amount'           => 'required|numeric|min:1000',
            'payment_method'   => 'required|in:cash,bank_transfer,momo',
            'transaction_code' => 'nullable|string|max:100',
            'notes'            => 'nullable|string'
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        // 1. Lưu lịch sử thanh toán
        $payment = Payment::create([
            'invoice_id'       => $invoice->id,
            'amount'           => $request->amount,
            'payment_method'   => $request->payment_method,
            'transaction_code' => $request->transaction_code,
            'notes'            => $request->notes,
        ]);

        // 2. Tính tổng tiền ĐÃ TRẢ cho hóa đơn này
        $totalPaid = $invoice->payments()->sum('amount');
        
        // 3. Nếu tiền đã trả >= tổng hóa đơn -> Cập nhật trạng thái Hóa đơn
        $isFullyPaid = false;
        if ($totalPaid >= $invoice->total_amount) {
            $invoice->update(['status' => 'paid']);
            $isFullyPaid = true;
        }

        return response()->json([
            'success' => true,
            'message' => 'Ghi nhận thanh toán thành công.',
            'data'    => $payment,
            'summary' => [
                'tong_hoa_don'  => $invoice->total_amount,
                'da_thanh_toan' => $totalPaid,
                'con_no'        => max(0, $invoice->total_amount - $totalPaid),
                'da_tat_toan'   => $isFullyPaid
            ]
        ], 201);
    }
}