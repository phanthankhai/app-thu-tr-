<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('password_reset_codes', function (Blueprint $table) {
            $table->id();
            $table->string('phone')->index(); // Số điện thoại nhận mã OTP
            $table->string('code'); // Mã OTP xác minh (đã mã hóa hoặc lưu thô tùy độ bảo mật)
            $table->timestamp('expires_at'); // Thời gian hết hạn của mã (ví dụ: 5 phút)
            $table->boolean('is_used')->default(false); // Trạng thái mã đã sử dụng chưa
            $table->timestamps(); // Thời gian tạo (created_at) dùng để tính thời gian gửi lại (Resend)
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('password_reset_codes');
    }
};