<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Service;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class ServiceController extends Controller
{
    // 1. Lấy danh sách toàn bộ dịch vụ
    public function index()
    {
        $services = Service::orderBy('created_at', 'desc')->get();
        return response()->json([
            'success' => true,
            'message' => 'Lấy danh sách dịch vụ thành công.',
            'data'    => $services
        ], 200);
    }

    // 2. Thêm dịch vụ mới
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name'  => 'required|string|max:255|unique:services,name', // Không cho tạo trùng tên
            'price' => 'required|numeric|min:0',
            'unit'  => 'required|string|max:50',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Dữ liệu không hợp lệ.',
                'errors'  => $validator->errors()
            ], 422);
        }

        $service = Service::create([
            'name'        => $request->name,
            'price'       => $request->price,
            'unit'        => $request->unit,
            'description' => $request->description
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Thêm dịch vụ thành công.',
            'data'    => $service
        ], 201);
    }

    // 3. Cập nhật dịch vụ (Đổi giá)
    public function update(Request $request, $id)
    {
        $service = Service::find($id);
        if (!$service) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy dịch vụ.'], 404);
        }

        $validator = Validator::make($request->all(), [
            'name'  => 'sometimes|string|max:255',
            'price' => 'sometimes|numeric|min:0',
            'unit'  => 'sometimes|string|max:50',
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, 'errors' => $validator->errors()], 422);
        }

        $service->update($request->all());

        return response()->json([
            'success' => true,
            'message' => 'Cập nhật dịch vụ thành công.',
            'data'    => $service
        ], 200);
    }

    // 4. Xóa dịch vụ
    public function destroy($id)
    {
        $service = Service::find($id);
        if (!$service) {
            return response()->json(['success' => false, 'message' => 'Không tìm thấy dịch vụ.'], 404);
        }

        $service->delete();

        return response()->json([
            'success' => true,
            'message' => 'Đã xóa dịch vụ thành công.'
        ], 200);
    }
}