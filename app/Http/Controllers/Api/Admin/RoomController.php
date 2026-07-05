<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Room;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Storage;
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
        'image'       => 'nullable|image|mimes:jpeg,png,jpg|max:2048', // Validate file ảnh
        'description' => 'nullable|string',
    ]);

    if ($validator->fails()) {
        return response()->json([
            'success' => false,
            'message' => 'Dữ liệu không hợp lệ.',
            'errors'  => $validator->errors()
        ], 422);
    }

    // Xử lý upload ảnh nếu có
    $imageUrl = null;
    if ($request->hasFile('image')) {
        $path = $request->file('image')->store('rooms', 'public');
        // Tạo đường dẫn URL đầy đủ (ví dụ: http://192.168.x.x:8000/storage/rooms/abc.png)
        $imageUrl = asset('storage/' . $path);
    }

    $room = Room::create([
        'name'        => $request->name,
        'price'       => $request->price,
        'area'        => $request->area,
        'status'      => $request->status ?? 'empty',
        'image'       => $imageUrl, // Lưu URL ảnh vào DB
        'description' => $request->description,
    ]);

    return response()->json([
        'success' => true,
        'message' => 'Thêm phòng mới thành công.',
        'data'    => $room
    ], 201);
}
    public function show($id)
    {
        // Tìm phòng theo ID
        $room = Room::find($id);

        // Nếu không tìm thấy phòng (ID tào lao)
        if (!$room) {
            return response()->json([
                'success' => false,
                'message' => 'Không tìm thấy phòng trọ này.'
            ], 404);
        }

        // Nếu tìm thấy, trả về đúng cục JSON mà Android đang chờ
        return response()->json([
            'success' => true,
            'message' => 'Lấy chi tiết phòng thành công.',
            'data'    => $room
        ], 200);

        $room = Room::with(['users' => function($query) {
        $query->where('role', 'tenant');
    }])->find($id);

    if (!$room) {
        return response()->json(['success' => false, 'message' => 'Không tìm thấy phòng'], 404);
    }

    return response()->json([
        'success' => true,
        'data'    => $room
    ], 200);
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
public function syncServices(Request $request, $id)
    {
        $request->validate([
            'service_ids'   => 'present|array', // Bắt buộc phải truyền mảng (có thể rỗng nếu muốn xóa hết dịch vụ)
            'service_ids.*' => 'exists:services,id' // Đảm bảo các ID truyền lên phải tồn tại trong bảng services
        ]);

        $room = \App\Models\Room::find($id);

        if (!$room) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy phòng'], 404);
        }

        // Hàm sync() tự động thêm ID mới và xóa ID cũ không có trong mảng
        $room->services()->sync($request->service_ids);

        return response()->json([
            'success' => true,
            'message' => 'Cập nhật dịch vụ cho phòng thành công!',
            'data'    => $room->load('services') // Trả về thông tin phòng kèm danh sách dịch vụ mới
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