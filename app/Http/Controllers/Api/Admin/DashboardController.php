<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Room;
use App\Models\User;
use App\Models\Bill;
use Illuminate\Http\Request;

class DashboardController extends Controller
{
    /**
     * API Thống kê Dashboard tổng quan
     */
    public function index(Request $request)
    {
        // 1. Số phòng trống (status = 'empty' dựa trên Enum của bạn)
        $emptyRooms = Room::where('status', 'empty')->count();

        // 2. Tổng số khách thuê
        $totalTenants = User::where('role', 'tenant')->count();

        // 3. Doanh thu tháng hiện tại (Sử dụng Collection sum để lấy giá trị từ Accessor)
        $currentMonth = date('m-Y');
        
        $revenue = Bill::where('billing_month', $currentMonth)
                       ->where('status', 'paid')
                       ->get() // Lấy dữ liệu về dưới dạng collection
                       ->sum('total_amount'); // Tận dụng bộ não tính toán trong Bill model

        // 4. Hóa đơn chưa đóng
        $unpaidBills = Bill::where('status', 'unpaid')->count();

        return response()->json([
            'success' => true,
            'message' => 'Lấy dữ liệu Dashboard thành công.',
            'data' => [
                'empty_rooms' => $emptyRooms,
                'total_tenants' => $totalTenants,
                'current_month_revenue' => (double) $revenue,
                'unpaid_bills_count' => $unpaidBills
            ]
        ], 200);
    }
}