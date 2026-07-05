<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Incident;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class IncidentController extends Controller
{
    /**
     * Lấy danh sách sự cố
     * Admin thấy tất cả, Khách thuê chỉ thấy sự cố của phòng mình
     */
    public function index()
    {
        $user = auth('api')->user();

        if ($user->role === 'admin') {
            $incidents = Incident::with('room')->orderBy('created_at', 'desc')->get();
        } else {
            // Chỉ lấy sự cố của phòng mà khách đang thuê
            $incidents = Incident::where('room_id', $user->room_id)
                                 ->orderBy('created_at', 'desc')
                                 ->get();
        }
        
        return response()->json([
            'success' => true,
            'data'    => $incidents
        ], 200);
    }

    /**
     * Người thuê báo cáo sự cố mới
     * Tự động lấy room_id từ tài khoản khách thuê
     */
    public function store(Request $request)
    {
        $user = auth('api')->user();

        // Kiểm tra xem khách có phòng chưa
        if (!$user->room_id) {
            return response()->json(['success' => false, 'message' => 'Bạn chưa được xếp phòng, không thể báo cáo.'], 403);
        }

        $validator = Validator::make($request->all(), [
            'title'       => 'required|string|max:255',
            'description' => 'required|string'
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        $incident = Incident::create([
            'room_id'     => $user->room_id, // Lấy trực tiếp từ User, khách không cần nhập
            'title'       => $request->title,
            'description' => $request->description,
            'status'      => 'pending' 
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Báo cáo sự cố thành công.',
            'data'    => $incident
        ], 201);
    }

    /**
     * Chủ trọ cập nhật trạng thái sự cố
     */
    public function updateStatus(Request $request, $id)
    {
        $incident = Incident::find($id);
        
        if (!$incident) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy phiếu báo cáo này.'], 404);
        }

        $validator = Validator::make($request->all(), [
            'status' => 'required|in:pending,in_progress,resolved'
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        $incident->update(['status' => $request->status]);

        return response()->json([
            'success' => true,
            'message' => 'Đã cập nhật trạng thái sự cố.',
            'data'    => $incident
        ], 200);
    }
}