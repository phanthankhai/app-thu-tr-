<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('contracts', function (Blueprint $table) {
            $table->id();
            // Khóa ngoại liên kết với bảng rooms
            $table->foreignId('room_id')->constrained('rooms')->onDelete('cascade'); 
            
            // Thông tin khách thuê đại diện
            $table->string('tenant_name');
            $table->string('tenant_phone');
            
            // Thông tin hợp đồng
            $table->decimal('deposit', 12, 2)->default(0); // Tiền cọc
            $table->date('start_date'); // Ngày dọn vào
            $table->enum('status', ['active', 'expired', 'cancelled'])->default('active'); // Trạng thái hợp đồng
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('contracts');
    }
};