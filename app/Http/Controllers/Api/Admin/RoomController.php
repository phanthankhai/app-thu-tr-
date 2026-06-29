<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Room;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class RoomController extends Controller
{
    /**
     * API Lấy danh sách tất cả các phòng
     */
    public function index()
    {
        // Lấy tất cả các phòng, sắp xếp phòng mới tạo lên đầu
        $rooms = Room::orderBy('created_at', 'desc')->get();

        return response()->json([
            'success' => true,
            'message' => 'Lấy danh sách phòng thành công.',
            'data'    => $rooms
        ], 200);
    }

    /**
     * API Thêm phòng mới
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name'        => 'required|string|max:255',
            'price'       => 'required|numeric|min:0',
            'area'        => 'nullable|integer|min:1',
            'status'      => 'nullable|in:empty,occupied,maintenance',
            'description' => 'nullable|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Dữ liệu không hợp lệ.',
                'errors'  => $validator->errors()
            ], 422);
        }

        $room = Room::create([
            'name'        => $request->name,
            'price'       => $request->price,
            'area'        => $request->area,
            'status'      => $request->status ?? 'empty',
            'description' => $request->description,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Thêm phòng mới thành công.',
            'data'    => $room
        ], 201);
    }

    /**
     * API Sửa thông tin phòng
     */
    public function update(Request $request, $id)
    {
        $room = Room::find($id);

        if (!$room) {
            return response()->json([
                'success' => false,
                'message' => 'Không tìm thấy phòng trọ này.'
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'name'        => 'sometimes|required|string|max:255',
            'price'       => 'sometimes|required|numeric|min:0',
            'area'        => 'sometimes|nullable|integer|min:1',
            'status'      => 'sometimes|in:empty,occupied,maintenance',
            'description' => 'sometimes|nullable|string',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Dữ liệu không hợp lệ.',
                'errors'  => $validator->errors()
            ], 422);
        }

        $room->update($request->all());

        return response()->json([
            'success' => true,
            'message' => 'Cập nhật thông tin phòng thành công.',
            'data'    => $room
        ], 200);
    }

    /**
     * API Xóa phòng
     */
    public function destroy($id)
    {
        $room = Room::find($id);

        if (!$room) {
            return response()->json([
                'success' => false,
                'message' => 'Không tìm thấy phòng trọ này.'
            ], 404);
        }

        $room->delete();

        return response()->json([
            'success' => true,
            'message' => 'Đã xóa phòng trọ thành công.'
        ], 200);
    }
}