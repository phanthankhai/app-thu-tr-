<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('phone_change_requests', function (Blueprint $table) {
            $table->id();
            // Liên kết với tài khoản đang thực hiện yêu cầu
            $table->unsignedBigInteger('user_id'); 
            $table->foreign('user_id')->references('id')->on('users')->onDelete('cascade');

            $table->string('old_phone'); // Lưu lại số điện thoại cũ để đối chiếu hoặc rollback khi cần
            $table->string('new_phone'); // Số điện thoại mới muốn thay đổi
            $table->string('verification_code'); // Mã OTP gửi vào số điện thoại MỚI để xác minh sở hữu
            
            // Trạng thái yêu cầu: 'pending' (đang chờ), 'verified' (đã xác minh), 'expired' (hết hạn)
            $table->string('status')->default('pending'); 
            
            $table->timestamp('expires_at'); // Thời gian mã OTP hết hiệu lực
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('phone_change_requests');
    }
};