<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            // Thay đổi cấu trúc mặc định của Laravel để phù hợp đặc tả
            $table->string('phone')->unique()->after('id'); // Số điện thoại làm tên đăng nhập cố định
            $table->string('role')->default('tenant')->after('phone'); // Vai trò: 'admin' hoặc 'tenant'
            
            // Khóa ngoại liên kết tới bảng phòng trọ (cho phép null nếu là Chủ trọ)
            $table->unsignedBigInteger('room_id')->nullable()->after('role'); 
            
            // Cờ bảo mật bắt buộc thiết lập mật khẩu mới ở lần đăng nhập đầu tiên
            $table->boolean('is_first_login')->default(true)->after('password');
            
            // Xóa trường email mặc định nếu hệ thống chỉ dùng số điện thoại đăng nhập công khai
            // $table->dropColumn('email'); 
        });
    }

    public function down(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->dropColumn(['phone', 'role', 'room_id', 'is_first_login']);
        });
    }
};