<?php

namespace App\Http\Controllers\Api\Admin;

use App\Http\Controllers\Controller;
use App\Models\Service;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class ServiceController extends Controller
{
    /**
     * API Lấy danh sách dịch vụ
     */
    public function index()
    {
        $services = Service::orderBy('created_at', 'desc')->get();

        return response()->json([
            'success' => true,
            'message' => 'Lấy danh sách dịch vụ thành công.',
            'data'    => $services
        ], 200);
    }

    /**
     * API Thêm dịch vụ mới (Điện, Nước, Rác...)
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name'        => 'required|string|max:255',
            'price'       => 'required|numeric|min:0',
            'unit'        => 'required|string|max:50',
            'description' => 'nullable|string',
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
            'description' => $request->description,
        ]);

        return response()->json([
            'success' => true,
            'message' => 'Thêm dịch vụ thành công.',
            'data'    => $service
        ], 201);
    }
}